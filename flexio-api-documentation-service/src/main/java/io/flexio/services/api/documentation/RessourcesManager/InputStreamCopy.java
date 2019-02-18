package io.flexio.services.api.documentation.RessourcesManager;

import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.io.*;
import java.util.UUID;

public class InputStreamCopy {
    private FileOutputStream fos;
    private File file;

    private static CategorizedLogger log = CategorizedLogger.getLogger(InputStreamCopy.class);


    public InputStreamCopy(InputStream is, String tmpDir) throws IOException {
        tmpDir += File.separator + "tmpInputStream" + UUID.randomUUID().toString();

        log.trace("New tmp File : " + tmpDir);

        this.fos = new FileOutputStream(tmpDir);

        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) > -1) {
            fos.write(buffer, 0, len);
        }
        fos.flush();

        this.file = new File(tmpDir);
    }

    public InputStream getCopy() throws FileNotFoundException {
        return new FileInputStream(file);
    }
}
