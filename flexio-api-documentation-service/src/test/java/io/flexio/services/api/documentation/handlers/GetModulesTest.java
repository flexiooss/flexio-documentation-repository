package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.DirectoryNotExistsException;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.RessourcesManager.TestRessourcesManager;
import io.flexio.services.api.documentation.api.ModulesGetRequest;
import io.flexio.services.api.documentation.api.ModulesGetResponse;
import io.flexio.services.api.documentation.api.types.Error;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class GetModulesTest {
    @Test
    public void noParameterRequest(){
        RessourcesManager fs = new TestRessourcesManager();
        ModulesGetRequest mgr = ModulesGetRequest.builder().build();
        ModulesGetResponse response = new GetModules(fs).apply(mgr);

        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void ok(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public List<String> getModules(String group) throws Exception {
                return new ArrayList<String>();
            }
        };
        ModulesGetRequest mgr = ModulesGetRequest.builder().group("g").build();
        ModulesGetResponse response = new GetModules(fs).apply(mgr);

        assertTrue(response.opt().status200().isPresent());
    }

    @Test
    public void okWithFiles(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public List<String> getModules(String group) throws Exception {
                List<String> list = new ArrayList<String>();
                list.add("plok");
                return list;
            }
        };
        ModulesGetRequest mgr = ModulesGetRequest.builder().group("g").build();
        ModulesGetResponse response = new GetModules(fs).apply(mgr);

        assertTrue(response.opt().status200().isPresent());
    }

    @Test
    public void okDirectoryException(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public List<String> getModules(String group) throws Exception {
                throw new DirectoryNotExistsException();
            }
        };
        ModulesGetRequest mgr = ModulesGetRequest.builder().group("g").build();
        ModulesGetResponse response = new GetModules(fs).apply(mgr);

        assertTrue(response.opt().status404().isPresent());
        assertThat(response.opt().status404().payload().code().get(), is(Error.Code.RESOURCE_NOT_FOUND));
    }

    @Test
    public void okInternalError(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public List<String> getModules(String group) throws Exception {
                throw new Exception();
            }
        };
        ModulesGetRequest mgr = ModulesGetRequest.builder().group("g").build();
        ModulesGetResponse response = new GetModules(fs).apply(mgr);

        assertTrue(response.opt().status500().isPresent());
    }


}