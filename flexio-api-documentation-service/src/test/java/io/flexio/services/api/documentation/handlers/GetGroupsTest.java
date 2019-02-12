package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.DirectoryNotExistsException;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.RessourcesManager.TestRessourcesManager;
import io.flexio.services.api.documentation.api.GroupsGetRequest;
import io.flexio.services.api.documentation.api.GroupsGetResponse;
import io.flexio.services.api.documentation.api.types.Error;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class GetGroupsTest {
    @Test
    public void okEmpty(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public List<String> getGroups() throws Exception {
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
            public List<String> getGroups() throws Exception {
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
            public List<String> getGroups() throws Exception {
                throw new DirectoryNotExistsException();
            }
        };

        GroupsGetRequest ggr = GroupsGetRequest.builder().build();
        GroupsGetResponse response = new GetGroups(fs).apply(ggr);

        assertTrue(response.opt().status500().isPresent());
    }

    @Test
    public void internalError(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public List<String> getGroups() throws Exception {
                throw new Exception();
            }
        };

        GroupsGetRequest ggr = GroupsGetRequest.builder().build();
        GroupsGetResponse response = new GetGroups(fs).apply(ggr);

        assertTrue(response.opt().status500().isPresent());
    }
}