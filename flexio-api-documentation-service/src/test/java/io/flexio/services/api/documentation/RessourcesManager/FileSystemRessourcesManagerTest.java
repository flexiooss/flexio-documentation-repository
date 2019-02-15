package io.flexio.services.api.documentation.RessourcesManager;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.InputStream;

public class FileSystemRessourcesManagerTest {
    private FileSystemRessourcesManager fs;

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp(){
        this.fs = new FileSystemRessourcesManager(tmpFolder.getRoot().getAbsolutePath());
    }

    @Test
    public void md5() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader);
        InputStream is = classLoader.getResourceAsStream("html.zip");
        assertNotNull(is);

        assertThat(fs.getmd5(is), is("c67e46795eca063e483f16c6fba5fab6"));
        is = classLoader.getResourceAsStream("html2Files.zip");

        assertThat(fs.getmd5(is), is("2121b2df6f35eab1599a5a29d9ec9f25"));

    }

    @Test
    public void addZipFile() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html.zip");
        assertNotNull(is);
        String pathBase = RessourcesManager.buildPath("g1", "m1", "v1", "c");

        ExtractZipResut result = fs.addZipFileIn(is, pathBase);
        assertThat(result.isExtracted(), is(true));
        assertThat(result.getPath(), is(pathBase));
        
        int nbRessources = fs.getRessources("g1", "m1", "v1", "c").size();
        assertThat(nbRessources, is(1));

        is = classLoader.getResourceAsStream("html.zip");
        pathBase = RessourcesManager.buildPath("g1", "m1", "v1", "c");
        result = fs.addZipFileIn(is, pathBase);
        assertThat(result.isExtracted(), is(false));
    }

    @Test
    public void testGetGroups() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader);
        InputStream is;

        assertThat(fs.getGroups().size(), is(0));

        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath("g1","m","v", "c"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath("g2","m","v", "c"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath("g3","m","v", "c"));

        assertThat(fs.getGroups().size(), is(3));
    }

    @Test
    public void testGetModulesExceptions() throws Exception {
        String group = "group1";

        thrown.expect(RessourceNotFoundException.class);
        assertThat(fs.getModules(group).size(), is(0));
    }


    @Test
    public void testGetModules() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader);
        InputStream is;

        String group = "group1";
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group,"m1","v", "c"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group,"m2","v", "c"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group,"m3","v", "c"));

        assertThat(fs.getModules(group).size(), is(3));
    }

    @Test
    public void testGetVersionsException() throws Exception {
        String group = "group1";
        String module = "module1";

        thrown.expect(RessourceNotFoundException.class);
        assertThat(fs.getVersions(group, module).size(), is(0));
    }

    @Test
    public void testGetVersions() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader);
        InputStream is;
        String group = "group1";
        String module = "module1";

        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group,module,"v1", "c"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group,module,"v2", "c"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group,module,"v3", "c"));

        assertThat(fs.getVersions(group, module).size(), is(3));
    }

    @Test
    public void testGetClassifiersExceptions() throws Exception {
        String group = "group1";
        String module = "module1";
        String version = "v1";
        thrown.expect(RessourceNotFoundException.class);
        assertThat(fs.getClassifiers(group, module, version).size(), is(0));
    }

    @Test
    public void testGetClassifiers() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is;
        String group = "group1";
        String module = "module1";
        String version = "v1";
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group,module,version, "c1"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group,module,version, "c2"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group,module,version, "c3"));

        assertThat(fs.getClassifiers(group, module, version).size(), is(3));
    }

    @Test
    public void exceptionRessources() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html2Files.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath("g", "m", "v1", "c"));

        thrown.expect(RessourceNotFoundException.class);
        fs.getRessources("g", "m", "v", "c2");
    }

    @Test
    public void exceptionClassifiers() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html2Files.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath("g", "m", "v1", "c"));
        thrown.expect(RessourceNotFoundException.class);
        fs.getClassifiers("g", "m", "v2");
    }

    @Test
    public void exceptionVersions() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html2Files.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath("g", "m", "v1", "c"));
        thrown.expect(RessourceNotFoundException.class);
        fs.getVersions("g", "m2");
    }
    @Test
    public void exceptionModules() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("html2Files.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath("g", "m", "v1", "c"));
        thrown.expect(RessourceNotFoundException.class);
        fs.getModules("g2");
        System.out.println("d");
    }

    @Test
    public void addZipWithMultipleFile() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is;
        String group = "group1";
        String module = "module1";
        String version = "v1";
        String classifier = "c";

        is = classLoader.getResourceAsStream("html2Files.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group,module,version, classifier));


        assertThat(fs.getRessources(group, module, version, classifier).size(), is(2));
    }

    @Test
    public void emptyZip() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("empty.zip");

        fs.addZipFileIn(is, RessourcesManager.buildPath("g", "m", "v", "c"));

        assertThat(fs.getRessources("g", "m", "v", "c").size(), is(0));
    }

    @Test
    public void zipWithManifestFile() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("Manifest.zip");

        fs.addZipFileIn(is, RessourcesManager.buildPath("g", "m", "v", "c"));

        assertThat(fs.getRessources("g", "m", "v", "c").size(), is(1));
    }
}
