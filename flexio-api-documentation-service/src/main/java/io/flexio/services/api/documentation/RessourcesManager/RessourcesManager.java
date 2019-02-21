package io.flexio.services.api.documentation.RessourcesManager;

import io.flexio.services.api.documentation.Exceptions.RessourceManagerException;
import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import io.flexio.services.api.documentation.api.types.Manifest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface RessourcesManager {
    String MANIFEST_FILE = "Manifest.json";

    ExtractZipResut addZipRessource(InputStream is, String group, String module, String version, String classifier) throws RessourceNotFoundException, RessourceManagerException;

    List<String> getGroups() throws RessourceNotFoundException;

    List<String> getModules(String group) throws RessourceNotFoundException;

    List<String> getVersions(String group, String module) throws RessourceNotFoundException;

    List<String> getClassifiers(String group, String module, String version) throws RessourceNotFoundException;

    List<String> getRessources(String group, String module, String version, String classifier) throws RessourceNotFoundException;

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
