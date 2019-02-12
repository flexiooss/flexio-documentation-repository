package io.flexio.services.api.documentation.RessourcesManager;

import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;

import java.io.InputStream;
import java.util.List;

public class TestRessourcesManager implements RessourcesManager {
    @Override
    public String addZipFileIn(InputStream is, String path) throws Exception{
        throw new AssertionError();
    }

    @Override
    public List<String> getGroups() throws Exception {
        throw new AssertionError();
    }

    @Override
    public List<String> getModules(String group) throws Exception {
        throw new AssertionError();
    }

    @Override
    public List<String> getVersions(String group, String module) throws Exception {
        throw new AssertionError();
    }

    @Override
    public List<String> getClassifiers(String group, String module, String version) throws Exception {
        throw new AssertionError();
    }

    @Override
    public List<String> getRessources(String group, String module, String version, String classifier) throws Exception {
        throw new AssertionError();
    }

    @Override
    public String getStorageDir() {
        throw new AssertionError();
    }
}
