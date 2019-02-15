package io.flexio.services.api.documentation.RessourcesManager;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flexio.services.api.documentation.Exceptions.RessourceManagerException;
import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import io.flexio.services.api.documentation.api.types.Manifest;
import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.io.*;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileSystemRessourcesManager implements RessourcesManager {
    private String STORAGE_DIR;
    private static CategorizedLogger log = CategorizedLogger.getLogger(FileSystemRessourcesManager.class);


    public FileSystemRessourcesManager(String storageDir) {
        this.STORAGE_DIR = storageDir;

        log.info("Storage dir : "+ this.STORAGE_DIR);
        File f = new File(STORAGE_DIR);
        if (!f.exists()){
            boolean created = f.mkdirs();
            //Todo throw exception ???
        }else{
            f.isDirectory();
        }
    }


    @Override
    public ExtractZipResut addZipFileIn(InputStream is, String path) throws RessourceNotFoundException, RessourceManagerException {
        try {
            CopyInputStream cis = new CopyInputStream(is);

            String md5 = getmd5(cis.getCopy());
            if (manifestFileExists(path)) {
                Manifest m = getManifest(path);
                if (md5.equals(m.md5())) {
                    return new ExtractZipResut(false, path);
                }
            }
            String finalPath = RessourcesManager.buildPath(STORAGE_DIR, path);

            ExtractZip ez = new ExtractZip(cis.getCopy(), finalPath);
            ez.extract();
            setManifest(md5, path);

            return new ExtractZipResut(true, path);
        } catch (IOException e){
            throw new RessourceManagerException("Error get/set manifest", e);
        }
    }

    @Override
    public List<String> getGroups() throws RessourceNotFoundException {
        return listFilesIn(STORAGE_DIR);
    }

    @Override
    public List<String> getModules(String group) throws RessourceNotFoundException {
        return listFilesIn(RessourcesManager.buildPath(STORAGE_DIR, group));
    }

    @Override
    public List<String> getVersions(String group, String module) throws RessourceNotFoundException {
        return listFilesIn(RessourcesManager.buildPath(STORAGE_DIR, group, module));
    }

    @Override
    public List<String> getClassifiers(String group, String module, String version) throws RessourceNotFoundException {
        return listFilesIn(RessourcesManager.buildPath(STORAGE_DIR, group, module, version));
    }

    @Override
    public List<String> getRessources(String group, String module, String version, String classifier) throws RessourceNotFoundException {
        return listFilesIn(RessourcesManager.buildPath(STORAGE_DIR, group, module, version, classifier));
    }


    private List<String> listFilesIn(String dir) throws RessourceNotFoundException {
        File base = new File(dir);
        log.trace("base path : " + base.getAbsolutePath());

        System.out.println("plath : "+base.getAbsolutePath());
        if (!base.exists()) {
            throw new RessourceNotFoundException();
        }

        List<String> list = new ArrayList<String>();
        File[] files = base.listFiles();
        for (File f : files) {
            if (!f.getName().equals(MANIFEST_FILE)) {
                list.add(f.getName());
                log.trace("File finded : " + f.getName());
            }
        }
        return list;
    }

    @Override
    public String getStorageDir() {
        return STORAGE_DIR;
    }

    @Override
    public String getmd5(InputStream is) throws IOException {
        String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
        return md5;
    }



    @Override
    public Manifest getManifest(String path) throws IOException {
        String finalPath = RessourcesManager.buildPath(this.STORAGE_DIR, path, MANIFEST_FILE);
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(Paths.get(finalPath).toFile());
        JsonNode nodeFiles = root.get("files");

        List<io.flexio.services.api.documentation.api.types.File> listFiles = new ArrayList<io.flexio.services.api.documentation.api.types.File>();
        for(JsonNode node : nodeFiles){
            listFiles.add(io.flexio.services.api.documentation.api.types.File.builder().name(node.textValue()).build());
            log.trace(node.textValue());
        }

        return Manifest.builder().md5(root.get("md5").asText())
                .files(listFiles)
                .uploadedAt(root.get("created-at").asText()).build();
    }

    @Override
    public void setManifest(String md5, String path) throws RessourceNotFoundException, IOException{
        String finalPath = RessourcesManager.buildPath(this.STORAGE_DIR, path);
        String finalPathJson = RessourcesManager.buildPath(finalPath, MANIFEST_FILE);
        List<String> listFiles = listFilesIn(finalPath);

        JsonGenerator jsonGenerator = new JsonFactory()
                .createGenerator(new FileOutputStream(finalPathJson));
        jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());

        jsonGenerator.writeStartObject(); // start root object

        ObjectMapper objectMapper = new ObjectMapper();

        jsonGenerator.writeStringField("md5", md5);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        jsonGenerator.writeStringField("created-at", dateFormat.format(date));

        jsonGenerator.writeArrayFieldStart("files");
        for(String file : listFiles){
            jsonGenerator.writeString(file);
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
        jsonGenerator.flush();
        jsonGenerator.close();
    }

    @Override
    public boolean manifestFileExists(String path){
        String finalPath = RessourcesManager.buildPath(this.STORAGE_DIR, path, MANIFEST_FILE);
        File f = new File(finalPath);
        return f.exists();
    }

    private InputStream copyInputStream(InputStream input) throws IOException{



        return null;
    }

}
