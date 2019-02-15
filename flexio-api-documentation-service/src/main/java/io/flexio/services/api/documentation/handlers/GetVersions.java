package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.api.VersionsGetRequest;
import io.flexio.services.api.documentation.api.VersionsGetResponse;
import io.flexio.services.api.documentation.api.types.Error;
import io.flexio.services.api.documentation.api.types.Version;
import io.flexio.services.api.documentation.api.versionsgetresponse.Status200;
import io.flexio.services.api.documentation.api.versionsgetresponse.Status400;
import io.flexio.services.api.documentation.api.versionsgetresponse.Status404;
import io.flexio.services.api.documentation.api.versionsgetresponse.Status500;
import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GetVersions implements Function<VersionsGetRequest, VersionsGetResponse> {
    private RessourcesManager fs;
    private static CategorizedLogger log = CategorizedLogger.getLogger(GetVersions.class);


    public GetVersions(RessourcesManager fs) {
        this.fs = fs;
    }

    @Override
    public VersionsGetResponse apply(VersionsGetRequest versionsGetRequest) {
        if (versionsGetRequest.opt().group().orElse("").isEmpty() ||
        versionsGetRequest.opt().module().orElse("").isEmpty()){
            return VersionsGetResponse.builder().status400(
                    Status400.builder().payload(
                            Error.builder().code(Error.Code.INCOMPLETE_REQUEST).build()
                    ).build()
            ).build();
        }

        try {
            List<String> versions = this.fs.getVersions(versionsGetRequest.group(), versionsGetRequest.module());
            List<Version> listVersions = new ArrayList<Version>();
            for (String version : versions) {
                listVersions.add(Version.builder().name(version).build());
            }

            return VersionsGetResponse.builder().status200(
                    Status200.builder().payload(listVersions).build()
            ).build();
        }catch (RessourceNotFoundException e){
            return VersionsGetResponse.builder().status404(
                    Status404.builder().payload(
                            Error.builder()
                                    .token(log.audit().tokenized().info("Dir not exists, Deleted ?", e))
                                    .code(Error.Code.RESOURCE_NOT_FOUND).build()
                    ).build()
            ).build();
        }catch (Exception e){
            return VersionsGetResponse.builder().status500(
                    Status500.builder().payload(
                            Error.builder()
                                    .token(log.audit().tokenized().info("Unknown error", e))
                                    .code(Error.Code.UNEXPECTED_ERROR).build()
                    ).build()
            ).build();
        }

    }
}
