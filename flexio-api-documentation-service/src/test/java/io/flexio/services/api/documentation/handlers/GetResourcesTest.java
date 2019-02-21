package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.ResourceNotFoundException;
import io.flexio.services.api.documentation.ResourcesManager.ResourcesManager;
import io.flexio.services.api.documentation.ResourcesManager.TestResourcesManager;
import io.flexio.services.api.documentation.api.FileGetRequest;
import io.flexio.services.api.documentation.api.FileGetResponse;
import io.flexio.services.api.documentation.api.types.Error;
import io.flexio.services.api.documentation.api.types.Manifest;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GetResourcesTest {

    @Test
    public void givenNoParameter__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        FileGetRequest fgr = FileGetRequest.builder().build();

        FileGetResponse response = new GetResources(fs).apply(fgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoModule__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .build();

        FileGetResponse response = new GetResources(fs).apply(fgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoVersion__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .module("m")
                .build();

        FileGetResponse response = new GetResources(fs).apply(fgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoClassifier__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .module("m")
                .version("v")
                .build();

        FileGetResponse response = new GetResources(fs).apply(fgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoFile__thenResponse20000() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getResources(String group, String module, String version, String classifier) throws ResourceNotFoundException {
                return new ArrayList<String>();
            }
        };

        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .module("m")
                .version("v")
                .classifier("c")
                .build();

        FileGetResponse response = new GetResources(fs).apply(fgr);
        assertTrue(response.opt().status200().isPresent());
    }

    @Test
    public void givenOkParameters__whenNoDir__thenResponse404() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getResources(String group, String module, String version, String classifier) throws ResourceNotFoundException {
                throw new ResourceNotFoundException();
            }
        };

        ClassLoader classLoader = getClass().getClassLoader();
        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .module("m")
                .version("v")
                .classifier("c")
                .build();

        FileGetResponse response = new GetResources(fs).apply(fgr);
        assertTrue(response.opt().status404().isPresent());
        assertThat(response.opt().status404().payload().code().get(), is(Error.Code.RESOURCE_NOT_FOUND));
    }

    @Test
    public void givenOkParameters__whenIOException__thenResponse500() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getResources(String group, String module, String version, String classifier) throws ResourceNotFoundException {
                return null;
            }

            @Override
            public Manifest getManifest(String path) throws FileNotFoundException, IOException {
                throw new IOException();
            }
        };

        ClassLoader classLoader = getClass().getClassLoader();
        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .module("m")
                .version("v")
                .classifier("c")
                .build();

        FileGetResponse response = new GetResources(fs).apply(fgr);
        assertTrue(response.opt().status500().isPresent());
    }
}