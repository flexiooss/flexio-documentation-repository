package io.flexio.services.api.documentation.RessourcesManager;

import io.flexio.services.api.documentation.Exceptions.RessourceManagerException;
import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import io.flexio.services.api.documentation.api.types.Manifest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestRessourcesManager implements RessourcesManager {
    @Override
    public ExtractZipResut addZipFileIn(InputStream is, String path) throws RessourceNotFoundException, RessourceManagerException {
        return null;
    }

    @Override
    public List<String> getGroups() throws RessourceNotFoundException {
        return null;
    }

    @Override
    public List<String> getModules(String group) throws RessourceNotFoundException {
        return null;
    }

    @Override
    public List<String> getVersions(String group, String module) throws RessourceNotFoundException {
        return null;
    }

    @Override
    public List<String> getClassifiers(String group, String module, String version) throws RessourceNotFoundException {
        return null;
    }

    @Override
    public List<String> getRessources(String group, String module, String version, String classifier) throws RessourceNotFoundException {
        return null;
    }

    @Override
    public String getStorageDir() {
        return null;
    }

    @Override
    public String getmd5(InputStream is) throws IOException {
        return null;
    }

    @Override
    public Manifest getManifest(String path) throws FileNotFoundException, IOException {
        return null;
    }

    @Override
    public void setManifest(String md5, String path) throws RessourceNotFoundException, IOException {

    }

    @Override
    public boolean manifestFileExists(String path) {
        return false;
    }
}
