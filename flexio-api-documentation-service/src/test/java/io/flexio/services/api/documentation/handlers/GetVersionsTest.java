package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.ResourceNotFoundException;
import io.flexio.services.api.documentation.ResourcesManager.ResourcesManager;
import io.flexio.services.api.documentation.ResourcesManager.TestResourcesManager;
import io.flexio.services.api.documentation.api.VersionsGetRequest;
import io.flexio.services.api.documentation.api.VersionsGetResponse;
import io.flexio.services.api.documentation.api.types.Error;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class GetVersionsTest {
    @Test
    public void givenNoParameter__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        VersionsGetRequest vgr = VersionsGetRequest.builder().build();

        VersionsGetResponse response = new GetVersions(fs).apply(vgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoModule__thenResponse400() {
        ResourcesManager fs = new TestResourcesManager();

        VersionsGetRequest vgr = VersionsGetRequest.builder()
                .group("g")
                .build();

        VersionsGetResponse response = new GetVersions(fs).apply(vgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void givenNoParameter__whenNoFile__thenResponse200() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getVersions(String group, String module) throws ResourceNotFoundException {
                return new ArrayList<String>();
            }
        };

        VersionsGetRequest vgr = VersionsGetRequest.builder()
                .group("g")
                .module("m")
                .build();

        VersionsGetResponse response = new GetVersions(fs).apply(vgr);
        assertTrue(response.opt().status200().isPresent());
        assertThat(response.opt().status200().payload().get().size(), is(0));
    }

    @Test
    public void givenNoParameter__when2File__thenResponse200() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getVersions(String group, String module) throws ResourceNotFoundException {
                List<String> list = new ArrayList<String>();
                list.add("plok");
                list.add("plokij");
                return list;
            }
        };

        VersionsGetRequest vgr = VersionsGetRequest.builder()
                .group("g")
                .module("m")
                .build();

        VersionsGetResponse response = new GetVersions(fs).apply(vgr);
        assertTrue(response.opt().status200().isPresent());
        assertThat(response.opt().status200().payload().get().size(), is(2));
    }

    @Test
    public void givenOkParameters__whenNoDir__thenResponse404() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getVersions(String group, String module) throws ResourceNotFoundException {
                throw new ResourceNotFoundException();
            }
        };

        VersionsGetRequest vgr = VersionsGetRequest.builder()
                .group("g")
                .module("m")
                .build();

        VersionsGetResponse response = new GetVersions(fs).apply(vgr);
        assertTrue(response.opt().status404().isPresent());
        assertThat(response.opt().status404().payload().code().get(), is(Error.Code.RESOURCE_NOT_FOUND));
    }
}