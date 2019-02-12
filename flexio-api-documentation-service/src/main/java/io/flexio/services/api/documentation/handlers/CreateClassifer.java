package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.DirectoryNotExistsException;
import io.flexio.services.api.documentation.RessourcesManager.FileSystemRessourcesManager;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.api.FilePostRequest;
import io.flexio.services.api.documentation.api.FilePostResponse;
import io.flexio.services.api.documentation.api.filepostresponse.Status201;
import io.flexio.services.api.documentation.api.filepostresponse.Status400;
import io.flexio.services.api.documentation.api.filepostresponse.Status404;
import io.flexio.services.api.documentation.api.filepostresponse.Status500;
import io.flexio.services.api.documentation.api.types.Error;

import java.io.InputStream;
import java.util.function.Function;

public class CreateClassifer implements Function<FilePostRequest, FilePostResponse> {
    private RessourcesManager fs;

    public CreateClassifer(RessourcesManager fs) {
        this.fs = fs;
    }

    @Override
    public FilePostResponse apply(FilePostRequest filesPostRequest) {
        if (filesPostRequest.opt().group().orElse("").isEmpty() ||
                filesPostRequest.opt().module().orElse("").isEmpty() ||
                filesPostRequest.opt().version().orElse("").isEmpty() ||
                filesPostRequest.opt().classifier().orElse("").isEmpty() ||
                !filesPostRequest.opt().payload().isPresent()){

            return FilePostResponse.builder().status400(
                    Status400.builder().payload(
                            Error.builder().code(Error.Code.INCOMPLETE_REQUEST).build()
                    ).build()
            ).build();
        }



        String path = RessourcesManager.buildPath(filesPostRequest.group(),
                filesPostRequest.module(),
                filesPostRequest.version(),
                filesPostRequest.classifier());
        try {
            InputStream is = filesPostRequest.payload().inputStream();
            String resPath = this.fs.addZipFileIn(is, path);
            return FilePostResponse.builder()
                    .status201(Status201.builder().location(resPath).build())
                    .build();
        }catch (DirectoryNotExistsException e){
            return FilePostResponse.builder().status404(
                    Status404.builder().payload(
                            Error.builder().code(Error.Code.RESOURCE_NOT_FOUND).build()
                    ).build()
            ).build();
        }catch (Exception e){
            e.printStackTrace();
            return FilePostResponse.builder()
                    .status500(Status500.builder().build()).build();
        }
    }
}
