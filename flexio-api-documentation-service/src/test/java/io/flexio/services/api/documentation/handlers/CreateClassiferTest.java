package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.RessourceManagerException;
import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import io.flexio.services.api.documentation.RessourcesManager.ExtractZipResut;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.RessourcesManager.TestRessourcesManager;
import io.flexio.services.api.documentation.api.FilePostRequest;
import io.flexio.services.api.documentation.api.FilePostResponse;
import org.codingmatters.rest.api.types.File;
import io.flexio.services.api.documentation.api.types.Error;
import org.codingmatters.rest.io.Content;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class CreateClassiferTest {

    @Test
    public void noParameterRequest(){
        RessourcesManager fs = new TestRessourcesManager();

        FilePostRequest fpr =  FilePostRequest.builder().build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void requestLackModule() {
        RessourcesManager fs = new TestRessourcesManager();

        FilePostRequest fpr =  FilePostRequest.builder()
                .group("g")
                .build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void requestLackVersion() {
        RessourcesManager fs = new TestRessourcesManager();

        FilePostRequest fpr =  FilePostRequest.builder()
                .group("g")
                .module("m")
                .classifier("c")
                .build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void requestLackClassifier() {
        RessourcesManager fs = new TestRessourcesManager();

        FilePostRequest fpr =  FilePostRequest.builder()
                .group("g")
                .module("m")
                .build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void requestLackFile(){
        RessourcesManager fs = new TestRessourcesManager();

        FilePostRequest fpr =  FilePostRequest.builder()
                .group("g")
                .module("m")
                .version("v1")
                .classifier("c")
                .build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void ok(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public ExtractZipResut addZipFileIn(InputStream is, String path) throws RessourceNotFoundException, RessourceManagerException {
                return new ExtractZipResut(true, path);
            }
        };

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html.zip");
        FilePostRequest fpr =  FilePostRequest.builder()
                .payload(
                        File.builder().content(Content.from(is)).build()
                )
                .group("g")
                .module("m")
                .classifier("c")
                .version("v1")
                .build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status201().isPresent());
    }

    @Test
    public void parametersOkInternalError(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public ExtractZipResut addZipFileIn(InputStream is, String path) throws RessourceNotFoundException, RessourceManagerException {
                throw new RessourceManagerException();
            }
        };

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html.zip");
        FilePostRequest fpr =  FilePostRequest.builder()
                .payload(
                        File.builder().content(Content.from(is)).build()
                )
                .group("g")
                .module("m")
                .classifier("c")
                .version("v1")
                .build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status500().isPresent());
    }

    @Test
    public void parametersOkNoDir(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public ExtractZipResut addZipFileIn(InputStream is, String path) throws RessourceNotFoundException, RessourceManagerException {
                throw new RessourceNotFoundException();
            }
        };

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html.zip");
        FilePostRequest fpr =  FilePostRequest.builder()
                .payload(
                        File.builder().content(Content.from(is)).build()
                )
                .group("g")
                .module("m")
                .classifier("c")
                .version("v1")
                .build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status404().isPresent());
        assertThat(response.opt().status404().payload().code().get(), is(Error.Code.RESOURCE_NOT_FOUND));
    }
}