package io.flexio.services.api.documentation.ResourcesManager;

import io.flexio.services.api.documentation.Exceptions.ResourceManagerException;
import io.flexio.services.api.documentation.Exceptions.ResourceNotFoundException;
import io.flexio.services.api.documentation.api.types.Manifest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ResourcesManager {
    String MANIFEST_FILE = "Manifest.json";

    ExtractZipResult addZipResource(InputStream is, String group, String module, String version, String classifier) throws ResourceNotFoundException, ResourceManagerException;

    List<String> getGroups() throws ResourceNotFoundException;

    List<String> getModules(String group) throws ResourceNotFoundException;

    List<String> getVersions(String group, String module) throws ResourceNotFoundException;

    List<String> getClassifiers(String group, String module, String version) throws ResourceNotFoundException;

    List<String> getResources(String group, String module, String version, String classifier) throws ResourceNotFoundException;

    String getmd5(InputStream is) throws NoSuchAlgorithmException, IOException;

    Manifest getManifest(String path) throws IOException;

    boolean manifestFileExists(String path);

    void updateLatest(String path, String groupe, String module) throws IOException;

    static String buildPath(String base, String... args){
        String path = base;
        for(String s : args){
            path += File.separator + s;
        }
        return path;
    }
}
