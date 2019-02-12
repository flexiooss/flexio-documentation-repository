package io.flexio.services.api.documentation.RessourcesManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

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
        System.out.println(tmpFolder.toString());
        System.out.println(tmpFolder.getRoot().getName());
        System.out.println(tmpFolder.getRoot().getAbsolutePath());

        ExtractZip ez = new ExtractZip(is, tmpFolder.getRoot().getAbsolutePath());
        ez.extract();
    }

    @Test
    public void extractMultipleFile() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html2Files.zip");
        System.out.println(tmpFolder.toString());
        System.out.println(tmpFolder.getRoot().getName());
        System.out.println(tmpFolder.getRoot().getAbsolutePath());

        ExtractZip ez = new ExtractZip(is, tmpFolder.getRoot().getAbsolutePath());
        ez.extract();
    }

}
