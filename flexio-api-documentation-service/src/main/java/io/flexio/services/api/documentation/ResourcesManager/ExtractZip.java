package io.flexio.services.api.documentation.ResourcesManager;

import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ExtractZip {
    private static CategorizedLogger log = CategorizedLogger.getLogger(ExtractZip.class);

    private InputStream is;
    private ZipInputStream zis;
    private String outputDir;

    public ExtractZip(InputStream is, String path) {
        this.is = is;
        this.outputDir = path;
        this.zis = new ZipInputStream(this.is);
    }

    public String extract() throws IOException {
        File folder = new File(this.outputDir);
        if(!folder.exists()){
            folder.mkdirs();
        }

        this.extractFiles();
        zis.close();

        return outputDir;
    }

    private void extractFiles() throws IOException{
        ZipEntry ze = zis.getNextEntry();

        while (ze != null){
            this.extract1File(ze);
            ze = zis.getNextEntry();
        }

        zis.closeEntry();
        log.trace("Unzip Done");
    }

    private void extract1File(ZipEntry ze) throws IOException{
        String fileName = ze.getName();
        File newFile = new File(this.outputDir + File.separator + fileName);
        new File(newFile.getParent()).mkdirs();

        FileOutputStream fos = new FileOutputStream(newFile);

        byte[] buffer = new byte[1024];
        int len;
        while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }

        fos.close();

        log.info("Unzip file : "+fileName);
        log.trace("Unzip "+newFile.getAbsolutePath());
    }


}