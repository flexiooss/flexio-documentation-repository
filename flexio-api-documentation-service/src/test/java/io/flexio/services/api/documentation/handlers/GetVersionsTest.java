package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.RessourcesManager.TestRessourcesManager;
import io.flexio.services.api.documentation.api.VersionsGetRequest;
import io.flexio.services.api.documentation.api.VersionsGetResponse;
import io.flexio.services.api.documentation.api.types.Error;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;


public class GetVersionsTest {
    @Test
    public void noParameterRequest(){
        RessourcesManager fs = new TestRessourcesManager();

        VersionsGetRequest vgr = VersionsGetRequest.builder().build();

        VersionsGetResponse response = new GetVersions(fs).apply(vgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void noModule(){
        RessourcesManager fs = new TestRessourcesManager();

        VersionsGetRequest vgr = VersionsGetRequest.builder()
                .group("g")
                .build();

        VersionsGetResponse response = new GetVersions(fs).apply(vgr);
        assertTrue(response.opt().status400().isPresent());
    }

    @Test
    public void okNoFile(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public List<String> getVersions(String group, String module) throws RessourceNotFoundException {
                return new ArrayList<String>();
            }
        };

        VersionsGetRequest vgr = VersionsGetRequest.builder()
                .group("g")
                .module("m")
                .build();

        VersionsGetResponse response = new GetVersions(fs).apply(vgr);
        assertTrue(response.opt().status200().isPresent());
    }

    @Test
    public void ok(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public List<String> getVersions(String group, String module) throws RessourceNotFoundException {
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
    public void okNoDirError(){
        RessourcesManager fs = new TestRessourcesManager(){
            @Override
            public List<String> getVersions(String group, String module) throws RessourceNotFoundException {
                throw new RessourceNotFoundException();
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