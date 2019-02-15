package io.flexio.services.api.documentation.RessourcesManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyInputStream {
    private ByteArrayOutputStream baos;

    public CopyInputStream(InputStream is) throws IOException {
        this.baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) > -1 ) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
    }

    public InputStream getCopy(){
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
