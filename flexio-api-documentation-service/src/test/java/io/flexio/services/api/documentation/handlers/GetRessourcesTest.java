package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.DirectoryNotExistsException;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.RessourcesManager.TestRessourcesManager;
import io.flexio.services.api.documentation.api.*;
import io.flexio.services.api.documentation.api.types.Error;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class GetRessourcesTest {

    @Test
    public void noParameterRequest(){
        RessourcesManager fs = new TestRessourcesManager();

        FileGetRequest fgr = FileGetRequest.builder().build();

        FileGetResponse response = new GetRessources(fs).apply(fgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void lackModule(){
        RessourcesManager fs = new TestRessourcesManager();

        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .build();

        FileGetResponse response = new GetRessources(fs).apply(fgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void lackVersion(){
        RessourcesManager fs = new TestRessourcesManager();

        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .module("m")
                .build();

        FileGetResponse response = new GetRessources(fs).apply(fgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void lackClassifier(){
        RessourcesManager fs = new TestRessourcesManager();

        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .module("m")
                .version("v")
                .build();

        FileGetResponse response = new GetRessources(fs).apply(fgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void okNoFile() {
        RessourcesManager fs = new TestRessourcesManager() {
            @Override
            public List<String> getRessources(String group, String module, String version, String classifier) throws Exception {
                return new ArrayList<String>();
            }
        };

        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .module("m")
                .version("v")
                .classifier("c")
                .build();

        FileGetResponse response = new GetRessources(fs).apply(fgr);
        assertTrue(response.opt().status200().isPresent());
    }

    @Test
    public void okMultipleFiles(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public List<String> getRessources(String group, String module, String version, String classifier) throws Exception {
                List<String> list = new ArrayList<String>();
                list.add("plok");
                list.add("plokij");
                return list;
            }
        };

        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .module("m")
                .version("v")
                .classifier("c")
                .build();

        FileGetResponse response = new GetRessources(fs).apply(fgr);
        assertTrue(response.opt().status200().isPresent());
        assertThat(response.opt().status200().payload().get().size(), is(2));
    }

    @Test
    public void okNoDirError(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public List<String> getRessources(String group, String module, String version, String classifier) throws Exception {
                throw new DirectoryNotExistsException();
            }
        };

        ClassLoader classLoader = getClass().getClassLoader();
        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .module("m")
                .version("v")
                .classifier("c")
                .build();

        FileGetResponse response = new GetRessources(fs).apply(fgr);
        assertTrue(response.opt().status404().isPresent());
        assertThat(response.opt().status404().payload().code().get(), is(Error.Code.RESOURCE_NOT_FOUND));
    }

    @Test
    public void okInternalError(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public List<String> getRessources(String group, String module, String version, String classifier) throws Exception {
                throw new Exception();
            }
        };

        ClassLoader classLoader = getClass().getClassLoader();
        FileGetRequest fgr = FileGetRequest.builder()
                .group("g")
                .module("m")
                .version("v")
                .classifier("c")
                .build();

        FileGetResponse response = new GetRessources(fs).apply(fgr);
        assertTrue(response.opt().status500().isPresent());
    }
}