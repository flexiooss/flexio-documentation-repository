package io.flexio.services.api.documentation.ResourcesManager;

import io.flexio.services.api.documentation.Exceptions.ResourceManagerException;
import io.flexio.services.api.documentation.Exceptions.ResourceNotFoundException;
import io.flexio.services.api.documentation.api.types.Manifest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestResourcesManager implements ResourcesManager {
    @Override
    public ExtractZipResult addZipResource(InputStream is, String group, String module, String version, String classifier) throws ResourceNotFoundException, ResourceManagerException {
        return null;
    }

    @Override
    public List<String> getGroups() throws ResourceNotFoundException {
        return null;
    }

    @Override
    public List<String> getModules(String group) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public List<String> getVersions(String group, String module) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public List<String> getClassifiers(String group, String module, String version) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public List<String> getResources(String group, String module, String version, String classifier) throws ResourceNotFoundException {
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
    public boolean manifestFileExists(String path) {
        return false;
    }

    @Override
    public void updateLatest(String path, String groupe, String module) throws IOException {

    }
}
