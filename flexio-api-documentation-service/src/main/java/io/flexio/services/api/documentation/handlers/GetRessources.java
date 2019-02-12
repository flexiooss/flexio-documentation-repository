package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.DirectoryNotExistsException;
import io.flexio.services.api.documentation.RessourcesManager.FileSystemRessourcesManager;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.api.FileGetRequest;
import io.flexio.services.api.documentation.api.FileGetResponse;
import io.flexio.services.api.documentation.api.filegetresponse.Status200;
import io.flexio.services.api.documentation.api.filegetresponse.Status400;
import io.flexio.services.api.documentation.api.filegetresponse.Status404;
import io.flexio.services.api.documentation.api.filegetresponse.Status500;
import io.flexio.services.api.documentation.api.types.Error;
import io.flexio.services.api.documentation.api.types.File;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GetRessources implements Function<FileGetRequest, FileGetResponse> {
    private RessourcesManager fs;

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

            List<File> listFiles = new ArrayList<File>();
            for (String file : files) {
                listFiles.add(File.builder().name(file).build());
            }

            return FileGetResponse.builder().status200(
                    Status200.builder().payload(listFiles).build()
            ).build();
        }catch (DirectoryNotExistsException e){
            return FileGetResponse.builder().status404(
                    Status404.builder().payload(
                            Error.builder().code(Error.Code.RESOURCE_NOT_FOUND).build()
                    ).build()
            ).build();
        }catch (Exception e){
            return FileGetResponse.builder().status500(
                    Status500.builder().build()
            ).build();
        }

    }
}
