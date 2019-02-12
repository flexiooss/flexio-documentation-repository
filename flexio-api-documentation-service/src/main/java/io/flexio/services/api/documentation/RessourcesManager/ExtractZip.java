package io.flexio.services.api.documentation.RessourcesManager;

import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.io.*;
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

    public String extract() throws Exception {
        File folder = new File(this.outputDir);
        if(!folder.exists()){
            folder.mkdir();
        }

        this.extractFiles();
        zis.close();

        return outputDir;
    }

    private void extractFiles() throws Exception{
        ZipEntry ze = zis.getNextEntry();

        while (ze != null){
            this.extract1File(ze);
            ze = zis.getNextEntry();
        }

        zis.closeEntry();
        log.trace("Unzip Done");
    }

    private void extract1File(ZipEntry ze) throws Exception{
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