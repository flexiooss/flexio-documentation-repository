package io.flexio.services.api.documentation.RessourcesManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.InputStream;

public class ExtractZipTest {
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void extract1File() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html.zip");

        ExtractZip ez = new ExtractZip(is, tmpFolder.getRoot().getAbsolutePath());
        String location = ez.extract();
        assertThat(location, is(tmpFolder.getRoot().getAbsolutePath()));
    }

    @Test
    public void extractMultipleFile() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html2Files.zip");


        ExtractZip ez = new ExtractZip(is, tmpFolder.getRoot().getAbsolutePath());
        String location = ez.extract();
        assertThat(location, is(tmpFolder.getRoot().getAbsolutePath()));
    }

}
