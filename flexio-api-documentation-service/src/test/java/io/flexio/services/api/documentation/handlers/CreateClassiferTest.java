package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.ResourceManagerException;
import io.flexio.services.api.documentation.Exceptions.ResourceNotFoundException;
import io.flexio.services.api.documentation.ResourcesManager.ExtractZipResult;
import io.flexio.services.api.documentation.ResourcesManager.ResourcesManager;
import io.flexio.services.api.documentation.ResourcesManager.TestResourcesManager;
import io.flexio.services.api.documentation.api.FilePostRequest;
import io.flexio.services.api.documentation.api.FilePostResponse;
import org.codingmatters.rest.api.types.File;
import org.codingmatters.rest.io.Content;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class CreateClassiferTest {

    @Test
    public void givenNoParameter__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        FilePostRequest fpr = FilePostRequest.builder().build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoModule__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        FilePostRequest fpr = FilePostRequest.builder()
                .group("g")
                .build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoVersion__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        FilePostRequest fpr = FilePostRequest.builder()
                .group("g")
                .module("m")
                .build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoClassifier__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        FilePostRequest fpr = FilePostRequest.builder()
                .group("g")
                .module("m")
                .classifier("c")
                .build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoFile__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        FilePostRequest fpr = FilePostRequest.builder()
                .group("g")
                .module("m")
                .version("v1")
                .classifier("c")
                .build();

        FilePostResponse response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenOkParameters__whenCallTwice__thenReponse201AndReponse200() {
        ResourcesManager fs = new TestResourcesManager() {
            private int cpt = 0;

            @Override
            public ExtractZipResult addZipResource(InputStream is, String group, String module, String version, String classifier) throws ResourceNotFoundException, ResourceManagerException {
                if (cpt == 0) {
                    cpt++;
                    return new ExtractZipResult(true, group);
                }
                return new ExtractZipResult(false, group);
            }
        };

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html.zip");
        FilePostRequest fpr = FilePostRequest.builder()
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


        //Send twice the same zip
        is = classLoader.getResourceAsStream("html.zip");
        fpr = FilePostRequest.builder()
                .payload(
                        File.builder().content(Content.from(is)).build()
                )
                .group("g")
                .module("m")
                .classifier("c")
                .version("v1")
                .build();
        response = new CreateClassifer(fs).apply(fpr);
        assertTrue(response.opt().status200().isPresent());
    }

    @Test
    public void givenOkParameters__whenInternalException__thenResponse500() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public ExtractZipResult addZipResource(InputStream is, String group, String module, String version, String classifier) throws ResourceNotFoundException, ResourceManagerException {
                throw new ResourceManagerException();
            }
        };

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html.zip");
        FilePostRequest fpr = FilePostRequest.builder()
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
    public void givenOkParameters__whenNoDir__thenResponse404() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public ExtractZipResult addZipResource(InputStream is, String group, String module, String version, String classifier) throws ResourceNotFoundException, ResourceManagerException {
                throw new ResourceManagerException();
            }
        };

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html.zip");
        FilePostRequest fpr = FilePostRequest.builder()
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
}