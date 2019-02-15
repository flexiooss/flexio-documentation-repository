package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.api.FileGetRequest;
import io.flexio.services.api.documentation.api.FileGetResponse;
import io.flexio.services.api.documentation.api.filegetresponse.Status200;
import io.flexio.services.api.documentation.api.filegetresponse.Status400;
import io.flexio.services.api.documentation.api.filegetresponse.Status404;
import io.flexio.services.api.documentation.api.filegetresponse.Status500;
import io.flexio.services.api.documentation.api.types.Error;
import io.flexio.services.api.documentation.api.types.Manifest;
import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.util.List;
import java.util.function.Function;

public class GetRessources implements Function<FileGetRequest, FileGetResponse> {
    private RessourcesManager fs;
    private static CategorizedLogger log = CategorizedLogger.getLogger(GetRessources.class);


    public GetRessources(RessourcesManager fs) {
        this.fs = fs;
    }

    @Override
    public FileGetResponse apply(FileGetRequest filesGetRequest) {
        if (filesGetRequest.opt().group().orElse("").isEmpty() ||
        filesGetRequest.opt().module().orElse("").isEmpty() ||
        filesGetRequest.opt().version().orElse("").isEmpty() ||
        filesGetRequest.opt().classifier().orElse("").isEmpty()){
            return FileGetResponse.builder().status400(
                    Status400.builder().payload(
                            Error.builder().code(Error.Code.INCOMPLETE_REQUEST).build()
                    ).build()
            ).build();
        }

        try {
            List<String> files = this.fs.getRessources(filesGetRequest.group(),
                    filesGetRequest.module(),
                    filesGetRequest.version(),
                    filesGetRequest.classifier());

            String path = RessourcesManager.buildPath(filesGetRequest.group(),
                    filesGetRequest.module(),
                    filesGetRequest.version(),
                    filesGetRequest.classifier());

                Manifest m = this.fs.getManifest(path);

            return FileGetResponse.builder().status200(
                    Status200.builder().payload(m).build()
            ).build();
        }catch (RessourceNotFoundException e){
            return FileGetResponse.builder().status404(
                    Status404.builder().payload(
                            Error.builder()
                                    .token(log.audit().tokenized().info("Dir not exists, Deleted ?", e))
                                    .code(Error.Code.RESOURCE_NOT_FOUND).build()
                    ).build()
            ).build();
        }catch (Exception e){
            return FileGetResponse.builder().status500(
                    Status500.builder().payload(
                            Error.builder()
                                    .token(log.audit().tokenized().info("Unknown error", e))
                                    .code(Error.Code.UNEXPECTED_ERROR).build()
                    ).build()
            ).build();
        }

    }
}
