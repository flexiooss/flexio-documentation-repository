package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.ResourceNotFoundException;
import io.flexio.services.api.documentation.ResourcesManager.ResourcesManager;
import io.flexio.services.api.documentation.ResourcesManager.TestResourcesManager;
import io.flexio.services.api.documentation.api.ModulesGetRequest;
import io.flexio.services.api.documentation.api.ModulesGetResponse;
import io.flexio.services.api.documentation.api.types.Error;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GetModulesTest {
    @Test
    public void givenNoParameter__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();
        ModulesGetRequest mgr = ModulesGetRequest.builder().build();
        ModulesGetResponse response = new GetModules(fs).apply(mgr);

        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoParameter__whenNoFile__thenResponse200() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getModules(String group) throws ResourceNotFoundException {
                return new ArrayList<String>();
            }
        };
        ModulesGetRequest mgr = ModulesGetRequest.builder().group("g").build();
        ModulesGetResponse response = new GetModules(fs).apply(mgr);

        assertTrue(response.opt().status200().isPresent());
        assertThat(response.opt().status200().payload().get().size(), is(0));

    }

    @Test
    public void givenNoParameter__when1File__thenResponse200() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getModules(String group) throws ResourceNotFoundException {
                List<String> list = new ArrayList<String>();
                list.add("plok");
                return list;
            }
        };
        ModulesGetRequest mgr = ModulesGetRequest.builder().group("g").build();
        ModulesGetResponse response = new GetModules(fs).apply(mgr);

        assertTrue(response.opt().status200().isPresent());
        assertThat(response.opt().status200().payload().get().size(), is(1));
    }

    @Test
    public void givenOkParameters__whenNoDir__thenResponse404() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getModules(String group) throws ResourceNotFoundException {
                throw new ResourceNotFoundException();
            }
        };
        ModulesGetRequest mgr = ModulesGetRequest.builder().group("g").build();
        ModulesGetResponse response = new GetModules(fs).apply(mgr);

        assertTrue(response.opt().status404().isPresent());
        assertThat(response.opt().status404().payload().code().get(), is(Error.Code.RESOURCE_NOT_FOUND));
    }

}