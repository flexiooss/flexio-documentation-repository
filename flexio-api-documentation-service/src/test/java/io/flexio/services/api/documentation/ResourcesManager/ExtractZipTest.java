package io.flexio.services.api.documentation.ResourcesManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.InputStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ExtractZipTest {
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void given1FileZipped__thenUnzip() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html.zip");

        ExtractZip ez = new ExtractZip(is, tmpFolder.getRoot().getAbsolutePath());
        String location = ez.extract();
        assertThat(location, is(tmpFolder.getRoot().getAbsolutePath()));
    }

    @Test
    public void givenMultipleFileZipped__thenUnzip() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html2Files.zip");


        ExtractZip ez = new ExtractZip(is, tmpFolder.getRoot().getAbsolutePath());
        String location = ez.extract();
        assertThat(location, is(tmpFolder.getRoot().getAbsolutePath()));
    }
}
