package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import io.flexio.services.api.documentation.RessourcesManager.ExtractZipResut;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.api.FilePostRequest;
import io.flexio.services.api.documentation.api.FilePostResponse;
import io.flexio.services.api.documentation.api.filepostresponse.*;
import io.flexio.services.api.documentation.api.types.Error;
import io.flexio.services.api.documentation.api.types.Manifest;
import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.io.InputStream;
import java.util.function.Function;

public class CreateClassifer implements Function<FilePostRequest, FilePostResponse> {
    private RessourcesManager fs;
    private static CategorizedLogger log = CategorizedLogger.getLogger(CreateClassifer.class);


    public CreateClassifer(RessourcesManager fs) {
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
                                    .token(log.tokenized().info("Lack of a parameter.s", filesPostRequest))
                                    .code(Error.Code.INCOMPLETE_REQUEST).build()
                    ).build()
            ).build();
        }


        String path = RessourcesManager.buildPath(filesPostRequest.group(),
                filesPostRequest.module(),
                filesPostRequest.version(),
                filesPostRequest.classifier());
        try {
            InputStream is = filesPostRequest.payload().inputStream();
            ExtractZipResut result = this.fs.addZipFileIn(is, path);
            Manifest m = this.fs.getManifest(path);


            if (result.isExtracted()) {
                return FilePostResponse.builder()
                        .status201(Status201.builder().payload(m).build()).build();
            } else {
                return FilePostResponse.builder().status200(
                        Status200.builder().payload(m).build()
                ).build();
            }
        } catch (RessourceNotFoundException e) {
            return FilePostResponse.builder().status404(
                    Status404.builder().payload(
                            Error.builder()
                                    .token(log.tokenized().info("Directory not exists", e))
                                    .code(Error.Code.RESOURCE_NOT_FOUND).build()
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
