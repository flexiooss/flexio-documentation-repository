package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.ResourceNotFoundException;
import io.flexio.services.api.documentation.ResourcesManager.ExtractZipResult;
import io.flexio.services.api.documentation.ResourcesManager.ResourcesManager;
import io.flexio.services.api.documentation.api.FilePostRequest;
import io.flexio.services.api.documentation.api.FilePostResponse;
import io.flexio.services.api.documentation.api.filepostresponse.*;
import io.flexio.services.api.documentation.api.types.Error;
import io.flexio.services.api.documentation.api.types.Manifest;
import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.io.InputStream;
import java.util.function.Function;

public class CreateClassifer implements Function<FilePostRequest, FilePostResponse> {
    private ResourcesManager fs;
    private static CategorizedLogger log = CategorizedLogger.getLogger(CreateClassifer.class);


    public CreateClassifer(ResourcesManager fs) {
        this.fs = fs;
    }

    @Override
    public FilePostResponse apply(FilePostRequest filesPostRequest) {
        if (filesPostRequest.opt().group().orElse("").isEmpty() ||
                filesPostRequest.opt().module().orElse("").isEmpty() ||
                filesPostRequest.opt().version().orElse("").isEmpty() ||
                filesPostRequest.opt().classifier().orElse("").isEmpty() ||
                !filesPostRequest.opt().payload().isPresent()) {

            return FilePostResponse.builder().status400(
                    Status400.builder().payload(
                            Error.builder()
                                    .token(log.tokenized().info("Lack of a parameter.s"))
                                    .code(Error.Code.INCOMPLETE_REQUEST).build()
                    ).build()
            ).build();
        }


        String group = filesPostRequest.group();
        String module = filesPostRequest.module();
        String version = filesPostRequest.version();
        String classifier = filesPostRequest.classifier();
        try {
            InputStream is = filesPostRequest.payload().inputStream();
            ExtractZipResult result = this.fs.addZipResource(is, group, module, version, classifier);
            Manifest m = this.fs.getManifest(ResourcesManager.buildPath(group, module, version, classifier));


            if (result.isExtracted()) {
                log.info("FilePostRequest ok, no extracted");
                return FilePostResponse.builder()
                        .status201(Status201.builder().payload(m).build()).build();
            } else {
                log.info("FilePostRequest ok, extracted");
                return FilePostResponse.builder().status200(
                        Status200.builder().payload(m).build()
                ).build();
            }
        } catch (ResourceNotFoundException e) {
            return FilePostResponse.builder().status404(
                    Status404.builder().payload(
                            Error.builder()
                                    .token(log.tokenized().info("ResourceManager error", e))
                                    .code(Error.Code.UNEXPECTED_ERROR).build()
                    ).build()
            ).build();
        } catch (Exception e) {
            e.printStackTrace();
            return FilePostResponse.builder()
                    .status500(Status500.builder().payload(
                            Error.builder()
                                    .token(log.tokenized().info("Unknown error", e))
                                    .code(Error.Code.UNEXPECTED_ERROR).build()
                    ).build()).build();
        }
    }
}
