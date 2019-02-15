package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.RessourceManagerException;
import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.RessourcesManager.TestRessourcesManager;
import io.flexio.services.api.documentation.api.GroupsGetRequest;
import io.flexio.services.api.documentation.api.GroupsGetResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GetGroupsTest {
    @Test
    public void okEmpty(){
        RessourcesManager fs = new TestRessourcesManager(){
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
    public void okFiles(){
        RessourcesManager fs = new TestRessourcesManager(){
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
    }

    @Test
    public void noDir(){
        RessourcesManager fs = new TestRessourcesManager(){
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