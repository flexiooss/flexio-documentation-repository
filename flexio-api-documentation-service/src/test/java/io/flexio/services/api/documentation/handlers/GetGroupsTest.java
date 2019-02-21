package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.ResourceNotFoundException;
import io.flexio.services.api.documentation.ResourcesManager.ResourcesManager;
import io.flexio.services.api.documentation.ResourcesManager.TestResourcesManager;
import io.flexio.services.api.documentation.api.GroupsGetRequest;
import io.flexio.services.api.documentation.api.GroupsGetResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GetGroupsTest {
    @Test
    public void givenNoParameter__whenPathExists__thenResponse200() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getGroups() throws ResourceNotFoundException {
                return new ArrayList<String>();
            }
        };

        GroupsGetRequest ggr = GroupsGetRequest.builder().build();
        GroupsGetResponse response = new GetGroups(fs).apply(ggr);

        assertTrue(response.opt().status200().isPresent());
    }

    @Test
    public void givenNoParameter__when1File__thenResponse200() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getGroups() throws ResourceNotFoundException {
                List<String> list = new ArrayList<String>();
                list.add("plok");
                return list;
            }
        };

        GroupsGetRequest ggr = GroupsGetRequest.builder().build();
        GroupsGetResponse response = new GetGroups(fs).apply(ggr);

        assertTrue(response.opt().status200().isPresent());
        assertThat(response.opt().status200().payload().get().size(), is(1));
    }

    @Test
    public void givenOkParameters__whenNoDir__thenResponse500() {
        ResourcesManager fs = new TestResourcesManager() {
            @Override
            public List<String> getGroups() throws ResourceNotFoundException {
                throw new ResourceNotFoundException();
            }
        };

        GroupsGetRequest ggr = GroupsGetRequest.builder().build();
        GroupsGetResponse response = new GetGroups(fs).apply(ggr);

        assertTrue(response.opt().status500().isPresent());
    }
}