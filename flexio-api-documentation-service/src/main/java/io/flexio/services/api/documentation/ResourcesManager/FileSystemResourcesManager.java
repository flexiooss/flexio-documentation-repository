package io.flexio.services.api.documentation.ResourcesManager;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flexio.services.api.documentation.Exceptions.ResourceManagerException;
import io.flexio.services.api.documentation.Exceptions.ResourceNotFoundException;
import io.flexio.services.api.documentation.Exceptions.VersionNotRecognizedException;
import io.flexio.services.api.documentation.api.types.Manifest;
import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileSystemResourcesManager implements ResourcesManager {
    private String STORAGE_DIR;
    private String MANIFEST_DIR;
    private String TMP_DIR;
    private String LATEST_DIR = "LATEST";
    private static CategorizedLogger log = CategorizedLogger.getLogger(FileSystemResourcesManager.class);
    private String LATEST_SNAPSHOT_DIR = "LATEST-SNAPSHOT";


    public FileSystemResourcesManager(String storageDir, String manifestDir, String tmpDir) {
        this.STORAGE_DIR = storageDir;
        this.MANIFEST_DIR = manifestDir;
        this.TMP_DIR = tmpDir;

        log.info("Storage dir : " + this.STORAGE_DIR);
        log.info("Manifest dir : " + this.MANIFEST_DIR);
        log.info("Tmp dir : " + this.TMP_DIR);

        File f;
        f = new File(STORAGE_DIR);
        f.mkdirs();

        f = new File(MANIFEST_DIR);
        f.mkdirs();

        f = new File(TMP_DIR);
        f.mkdirs();
    }


    @Override
    public ExtractZipResult addZipResource(InputStream is, String group, String module, String version, String classifier) throws ResourceNotFoundException, ResourceManagerException {
        try {
            VersionExtractor ve = new VersionExtractor(version);
            ve.parse();
            String versionParsed = ve.prettyPrint();
            String pathResource = ResourcesManager.buildPath(group, module, versionParsed, classifier);

            try (InputStreamCopy cis = new InputStreamCopy(is, this.TMP_DIR)) {
                //Hash the zip file and check if matches with the md5 in the Manifest
                String md5 = getmd5(cis.getCopy());
                if (manifestFileExists(pathResource)) {
                    Manifest m = getManifest(pathResource);
                    if (md5.equals(m.md5())) {
                        return new ExtractZipResult(false, pathResource);
                    }
                }
                String finalPath = ResourcesManager.buildPath(STORAGE_DIR, pathResource);

                ExtractZip ez = new ExtractZip(cis.getCopy(), finalPath);
                ez.extract();
                setManifest(md5, pathResource);

                updateLatestDirIfNecessary(group, module, ve);
                updateLatestSnapshotDirIfNecessary(group, module, ve);

                return new ExtractZipResult(true, pathResource);
            }
        } catch (IOException e) {
            throw new ResourceManagerException("Error get/set manifest", e);
        } catch (VersionNotRecognizedException e) {
            throw new ResourceManagerException("Error parse version", e);
        }

    }

    @Override
    public List<String> getGroups() throws ResourceNotFoundException {
        return listFilesIn(STORAGE_DIR);
    }

    @Override
    public List<String> getModules(String group) throws ResourceNotFoundException {
        String path = ResourcesManager.buildPath(STORAGE_DIR, group);
        return listFilesIn(path);
    }

    @Override
    public List<String> getVersions(String group, String module) throws ResourceNotFoundException {
        String path = ResourcesManager.buildPath(STORAGE_DIR, group, module);
        return listFilesIn(path);
    }

    @Override
    public List<String> getClassifiers(String group, String module, String version) throws ResourceNotFoundException {
        String path = ResourcesManager.buildPath(STORAGE_DIR, group, module, version);
        return listFilesIn(path);
    }

    @Override
    public List<String> getResources(String group, String module, String version, String classifier) throws ResourceNotFoundException {
        String path = ResourcesManager.buildPath(STORAGE_DIR, group, module, version, classifier);
        return listFilesIn(path);
    }


    private List<String> listFilesIn(String dir) throws ResourceNotFoundException {
        File base = new File(dir);
        log.trace("base path : " + base.getAbsolutePath());
        if (!base.exists()) {
            throw new ResourceNotFoundException();
        }

        List<String> list = new ArrayList<String>();
        File[] files = base.listFiles();
        for (File f : files) {
            list.add(f.getName());
            log.trace("File finded : " + f.getName());
        }
        return list;
    }


    @Override
    public String getmd5(InputStream is) throws IOException {
        String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
        return md5;
    }

    @Override
    public Manifest getManifest(String path) throws IOException {
        String finalPath = manifestFilePath(path);
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(Paths.get(finalPath).toFile());
        JsonNode nodeFiles = root.get("files");

        List<io.flexio.services.api.documentation.api.types.File> listFiles = new ArrayList<io.flexio.services.api.documentation.api.types.File>();
        for (JsonNode node : nodeFiles) {
            listFiles.add(io.flexio.services.api.documentation.api.types.File.builder().name(node.textValue()).build());
            log.trace(node.textValue());
        }

        return Manifest.builder().md5(root.get("md5").asText())
                .files(listFiles)
                .uploadedAt(root.get("created-at").asText()).build();
    }


    private void setManifest(String md5, String path) throws ResourceNotFoundException, IOException {
        String pathFiles = ResourcesManager.buildPath(this.STORAGE_DIR, path);
        String finalPathManifest = manifestFilePath(path);
        List<String> listFiles = listFilesIn(pathFiles);

        File f = new File(ResourcesManager.buildPath(this.MANIFEST_DIR, path));
        if (!f.exists())
            f.mkdirs();


        try (FileOutputStream fos = new FileOutputStream(finalPathManifest)) {
            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(fos);
            jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());

            jsonGenerator.writeStartObject(); // start root object

            jsonGenerator.writeStringField("md5", md5);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            jsonGenerator.writeStringField("created-at", dateFormat.format(date));

            jsonGenerator.writeArrayFieldStart("files");
            for (String file : listFiles) {
                jsonGenerator.writeString(file);
            }
            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndObject();
            jsonGenerator.flush();
        }
    }

    @Override
    public boolean manifestFileExists(String path) {
        String finalPath = manifestFilePath(path);

        File f = new File(finalPath);
        return f.exists();
    }

    private String manifestFilePath(String path) {
        return ResourcesManager.buildPath(this.MANIFEST_DIR, path, MANIFEST_FILE);
    }

    private void updateLatestDirIfNecessary(String groupe, String module, VersionExtractor ve) throws ResourceManagerException {
        try {
            String latestVersion = getLatestVersion(groupe, module);
            VersionExtractor latestVe = new VersionExtractor(latestVersion);
            latestVe.parse();
            if (ve.compareTo(latestVe) >= 0) {
                updateLatest(groupe, module, ve.prettyPrint());
            }
        } catch (NoSuchFileException e) {
            log.trace("latest dir does not exists, created");
            try {
                updateLatest(groupe, module, ve.prettyPrint());
            } catch (IOException e2) {
                throw new ResourceManagerException("Error updating latest dir", e2);
            }
        } catch (IOException e) {
            throw new ResourceManagerException("Error updating latest dir", e);
        } catch (VersionNotRecognizedException e) {
            throw new ResourceManagerException("Error parsing version latest dir");
        }
    }

    private void updateLatestSnapshotDirIfNecessary(String groupe, String module, VersionExtractor ve) throws ResourceManagerException {
        try {
            String latestSnapshotVersion = getLatestSnapshotVersion(groupe, module);
            VersionExtractor latestSnapshotVe = new VersionExtractor(latestSnapshotVersion);
            latestSnapshotVe.parse();
            if (ve.compareTo(latestSnapshotVe) >= 0) {
                updateLatestSnapshot(groupe, module, ve.prettyPrint());
            }
        } catch (NoSuchFileException e) {
            log.trace("latest dir does not exists, created");
            try {
                updateLatestSnapshot(groupe, module, ve.prettyPrint());
            } catch (IOException e2) {
                throw new ResourceManagerException("Error updating latest dir", e2);
            }
        } catch (IOException e) {
            throw new ResourceManagerException("Error updating latest dir", e);
        } catch (VersionNotRecognizedException e) {
            throw new ResourceManagerException("Error parsing version latest dir");
        }
    }

    @Override
    public void updateLatest(String groupe, String module, String version) throws IOException {
        Path source = Paths.get(this.STORAGE_DIR, groupe, module, version);
        log.trace("src " + source);

        Path link = Paths.get(this.STORAGE_DIR, groupe, module, this.LATEST_DIR);
        log.trace("latest " + link);
        if (Files.exists(link)) {
            Files.delete(link);
        }

        Files.createSymbolicLink(link, source);
    }


    public void updateLatestSnapshot(String groupe, String module, String version) throws IOException {
        Path source = Paths.get(this.STORAGE_DIR, groupe, module, version);
        log.trace("src " + source);

        Path link = Paths.get(this.STORAGE_DIR, groupe, module, this.LATEST_SNAPSHOT_DIR);
        log.trace("latest snapshot" + link);
        if (Files.exists(link)) {
            Files.delete(link);
        }

        Files.createSymbolicLink(link, source);
    }

    private String getLatestVersion(String groupe, String module) throws IOException {
        Path link = Paths.get(this.STORAGE_DIR, groupe, module, this.LATEST_DIR);
        Path l = Files.readSymbolicLink(link);
        String v = l.toString().substring(l.toString().lastIndexOf('/') + 1);
        log.trace("Latest " + v);
        return v;
    }

    private String getLatestSnapshotVersion(String groupe, String module) throws IOException {
        Path link = Paths.get(this.STORAGE_DIR, groupe, module, this.LATEST_SNAPSHOT_DIR);
        Path l = Files.readSymbolicLink(link);
        String v = l.toString().substring(l.toString().lastIndexOf('/') + 1);
        log.trace("Latest " + v);
        return v;
    }

}
