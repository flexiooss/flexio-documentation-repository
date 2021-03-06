package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.ResourceNotFoundException;
import io.flexio.services.api.documentation.ResourcesManager.ResourcesManager;
import io.flexio.services.api.documentation.ResourcesManager.TestResourcesManager;
import io.flexio.services.api.documentation.api.ClassifiersGetRequest;
import io.flexio.services.api.documentation.api.ClassifiersGetResponse;
import io.flexio.services.api.documentation.api.types.Error;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class GetClassifiersTest {
    @Test
    public void givenNoParameter__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        ClassifiersGetRequest cgr = ClassifiersGetRequest.builder().build();

        ClassifiersGetResponse response = new GetClassifiers(fs).apply(cgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoModule__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        ClassifiersGetRequest cgr = ClassifiersGetRequest.builder()
                .group("g")
                .build();

        ClassifiersGetResponse response = new GetClassifiers(fs).apply(cgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoVersion__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        ClassifiersGetRequest cgr = ClassifiersGetRequest.builder()
                .group("g")
                .module("m")
                .build();

        ClassifiersGetResponse response = new GetClassifiers(fs).apply(cgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoClassifier__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getClassifiers(String group, String module, String version) throws ResourceNotFoundException {
                return new ArrayList<String>();
            }
        };

        ClassifiersGetRequest cgr = ClassifiersGetRequest.builder()
                .group("g")
                .module("m")
                .version("v1")
                .build();

        ClassifiersGetResponse response = new GetClassifiers(fs).apply(cgr);
        assertTrue(response.opt().status200().isPresent());
    }

    @Test
    public void givenOkParameters__thenReponse200() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getClassifiers(String group, String module, String version) throws ResourceNotFoundException {
                List<String> list = new ArrayList<String>();
                list.add("plok");
                list.add("plokij");
                return list;
            }
        };

        ClassifiersGetRequest cgr = ClassifiersGetRequest.builder()
                .group("g")
                .module("m")
                .version("v1")
                .build();

        ClassifiersGetResponse response = new GetClassifiers(fs).apply(cgr);
        assertTrue(response.opt().status200().isPresent());
        assertThat(response.opt().status200().payload().get().size(), is(2));
    }

    @Test
    public void givenOkParameters__whenNoDir__thenResponse404() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getClassifiers(String group, String module, String version) throws ResourceNotFoundException {
                throw new ResourceNotFoundException();
            }
        };

        ClassifiersGetRequest cgr = ClassifiersGetRequest.builder()
                .group("g")
                .module("m")
                .version("v1")
                .build();

        ClassifiersGetResponse response = new GetClassifiers(fs).apply(cgr);
        assertTrue(response.opt().status404().isPresent());
        assertThat(response.opt().status404().payload().code().get(), is(Error.Code.RESOURCE_NOT_FOUND));

    }

}