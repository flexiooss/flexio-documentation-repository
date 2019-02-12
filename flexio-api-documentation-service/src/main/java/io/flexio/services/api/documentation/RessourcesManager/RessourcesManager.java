package io.flexio.services.api.documentation.RessourcesManager;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface RessourcesManager {
    String addZipFileIn(InputStream is, String path) throws Exception;

    List<String> getGroups() throws Exception;

    List<String> getModules(String group) throws Exception;

    List<String> getVersions(String group, String module) throws Exception;

    List<String> getClassifiers(String group, String module, String version) throws  Exception;

    List<String> getRessources(String group, String module, String version, String classifier) throws  Exception;

    String getStorageDir();

    public static String buildPath(String... args){
        String path = "";
        for(String s : args){
            path += File.separator + s;
        }
        return path;
    }
}
