package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.RessourcesManager.TestRessourcesManager;
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
        RessourcesManager fs = new TestRessourcesManager() {
            @Override
            public List<String> getGroups() throws RessourceNotFoundException {
                return new ArrayList<String>();
            }
        };

        GroupsGetRequest ggr = GroupsGetRequest.builder().build();
        GroupsGetResponse response = new GetGroups(fs).apply(ggr);

        assertTrue(response.opt().status200().isPresent());
    }

    @Test
    public void givenNoParameter__when1File__thenResponse200() {
        RessourcesManager fs = new TestRessourcesManager() {
            @Override
            public List<String> getGroups() throws RessourceNotFoundException {
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
        RessourcesManager fs = new TestRessourcesManager() {
            @Override
            public List<String> getGroups() throws RessourceNotFoundException {
                throw new RessourceNotFoundException();
            }
        };

        GroupsGetRequest ggr = GroupsGetRequest.builder().build();
        GroupsGetResponse response = new GetGroups(fs).apply(ggr);

        assertTrue(response.opt().status500().isPresent());
    }
}