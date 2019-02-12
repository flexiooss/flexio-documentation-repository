package io.flexio.services.api.documentation.RessourcesManager;

import io.flexio.services.api.documentation.Exceptions.DirectoryNotExistsException;
import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRessourcesManager implements RessourcesManager {
    private String STORAGE_DIR;
    private static CategorizedLogger log = CategorizedLogger.getLogger(FileSystemRessourcesManager.class);


    public FileSystemRessourcesManager(String storageDir) {
        this.STORAGE_DIR = storageDir;

        log.info("Storage dir : "+ this.STORAGE_DIR);
        File f = new File(STORAGE_DIR);
    }


    @Override
    public String addZipFileIn(InputStream is, String path) throws Exception{
        String finalPath = this.STORAGE_DIR + path;

        return new ExtractZip(is, finalPath).extract();
    }

    @Override
    public List<String> getGroups() throws Exception{
        return listFilesIn(STORAGE_DIR);
    }

    @Override
    public List<String> getModules(String group) throws Exception{
        return listFilesIn(RessourcesManager.buildPath(STORAGE_DIR, group));
    }

    @Override
    public List<String> getVersions(String group, String module) throws Exception{
        return listFilesIn(RessourcesManager.buildPath(STORAGE_DIR, group, module));
    }

    @Override
    public List<String> getClassifiers(String group, String module, String version) throws  Exception{
        return listFilesIn(RessourcesManager.buildPath(STORAGE_DIR, group, module, version));
    }

    @Override
    public List<String> getRessources(String group, String module, String version, String classifier) throws  Exception{
        return listFilesIn(RessourcesManager.buildPath(STORAGE_DIR, group, module, version, classifier));
    }


    private List<String> listFilesIn(String dir) throws Exception{
        File base = new File(dir);
        log.trace("base path : " + base.getAbsolutePath());
        if (!base.exists()) {
            throw new DirectoryNotExistsException();
        }

        List<String> list = new ArrayList<String>();
        for (File f : base.listFiles()) {
            list.add(f.getName());
            log.trace(f.getName());
        }
        return list;
    }

    @Override
    public String getStorageDir() {
        return STORAGE_DIR;
    }
}
