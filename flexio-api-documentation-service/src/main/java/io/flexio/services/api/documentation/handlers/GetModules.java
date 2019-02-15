package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.api.ModulesGetRequest;
import io.flexio.services.api.documentation.api.ModulesGetResponse;
import io.flexio.services.api.documentation.api.modulesgetresponse.Status200;
import io.flexio.services.api.documentation.api.modulesgetresponse.Status400;
import io.flexio.services.api.documentation.api.modulesgetresponse.Status404;
import io.flexio.services.api.documentation.api.modulesgetresponse.Status500;
import io.flexio.services.api.documentation.api.types.Error;
import io.flexio.services.api.documentation.api.types.Module;
import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GetModules implements Function<ModulesGetRequest, ModulesGetResponse> {
    private RessourcesManager fs;
    private static CategorizedLogger log = CategorizedLogger.getLogger(GetModules.class);


    public GetModules(RessourcesManager fs) {
        this.fs = fs;
    }

    @Override
    public ModulesGetResponse apply(ModulesGetRequest modulesGetRequest) {
        if (modulesGetRequest.opt().group().orElse("").isEmpty()){
            return ModulesGetResponse.builder().status400(
                    Status400.builder().payload(
                            Error.builder().code(Error.Code.INCOMPLETE_REQUEST).build()
                    ).build()
            ).build();
        }

        try {

            List<String> modules = this.fs.getModules(modulesGetRequest.group());

            List<Module> listModules = new ArrayList<Module>();
            for (String module : modules){
                listModules.add(Module.builder().name(module).build());
            }

            return ModulesGetResponse.builder().status200(
                    Status200.builder().payload(listModules).build()
            ).build();
        }catch (RessourceNotFoundException e){
            return ModulesGetResponse.builder().status404(
                    Status404.builder().payload(
                            Error.builder()
                                    .token(log.audit().tokenized().info("Dir not exists, Deleted ?", e))
                                    .code(Error.Code.RESOURCE_NOT_FOUND).build()
                    ).build()
            ).build();
        }catch (Exception e){
            return ModulesGetResponse.builder().status500(
                    Status500.builder().payload(
                            Error.builder()
                                    .token(log.audit().tokenized().info("Unknown error", e))
                                    .code(Error.Code.UNEXPECTED_ERROR).build()
                    ).build()
            ).build();
        }
    }
}
