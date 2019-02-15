package io.flexio.services.api.documentation.RessourcesManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CopyInputStreamTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader);
        InputStream is = classLoader.getResourceAsStream("html.zip");
        int nb = is.available();
        CopyInputStream cis = new CopyInputStream(is);
        assertThat(nb, is(cis.getCopy().available()));
    }

}