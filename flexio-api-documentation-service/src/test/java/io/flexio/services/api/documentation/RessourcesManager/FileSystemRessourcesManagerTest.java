package io.flexio.services.api.documentation.RessourcesManager;

import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.InputStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class FileSystemRessourcesManagerTest {
    private FileSystemRessourcesManager fs;

    @Rule
    public TemporaryFolder tmpFolderStorage = new TemporaryFolder();
    @Rule
    public TemporaryFolder tmpFolderManifest = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        this.fs = new FileSystemRessourcesManager(
                tmpFolderStorage.getRoot().getAbsolutePath(),
                tmpFolderManifest.getRoot().getAbsolutePath());
    }

    @Test
    public void given1Zip__thenMDMatches() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader);
        InputStream is = classLoader.getResourceAsStream("html.zip");
        assertNotNull(is);

        assertThat(fs.getmd5(is), is("c67e46795eca063e483f16c6fba5fab6"));
        is = classLoader.getResourceAsStream("html2Files.zip");

        assertThat(fs.getmd5(is), is("2121b2df6f35eab1599a5a29d9ec9f25"));
    }

    @Test
    public void given1Zip__whenCalledTwice__then1FileExtractedOnlyTheFirstTime() throws Exception {
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
    public void givenSend3Files__then3Groups() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader);
        InputStream is;

        assertThat(fs.getGroups().size(), is(0));

        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath("g1", "m", "v", "c"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath("g2", "m", "v", "c"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath("g3", "m", "v", "c"));

        assertThat(fs.getGroups().size(), is(3));
    }

    @Test
    public void whenNoDir__thenThrowRessourceNotFound0Group() throws Exception {
        String group = "group1";

        thrown.expect(RessourceNotFoundException.class);
        assertThat(fs.getModules(group).size(), is(0));
    }


    @Test
    public void givenSend3Files__then3Modules() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader);
        InputStream is;

        String group = "group1";
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group, "m1", "v", "c"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group, "m2", "v", "c"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group, "m3", "v", "c"));

        assertThat(fs.getModules(group).size(), is(3));
    }

    @Test
    public void whenNoDir__thenThrowRessourceNotFound0Version() throws Exception {
        String group = "group1";
        String module = "module1";

        thrown.expect(RessourceNotFoundException.class);
        assertThat(fs.getVersions(group, module).size(), is(0));
    }

    @Test
    public void givenSend3Files__then3Versions() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader);
        InputStream is;
        String group = "group1";
        String module = "module1";

        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group, module, "v1", "c"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group, module, "v2", "c"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group, module, "v3", "c"));

        assertThat(fs.getVersions(group, module).size(), is(3));
    }

    @Test
    public void whenNoDir__thenThrowRessourceNotFound0Classifier() throws Exception {
        String group = "group1";
        String module = "module1";
        String version = "v1";
        thrown.expect(RessourceNotFoundException.class);
        assertThat(fs.getClassifiers(group, module, version).size(), is(0));
    }

    @Test
    public void givenSend3Files__then3Classifiers() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is;
        String group = "group1";
        String module = "module1";
        String version = "v1";
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group, module, version, "c1"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group, module, version, "c2"));
        is = classLoader.getResourceAsStream("html.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group, module, version, "c3"));

        assertThat(fs.getClassifiers(group, module, version).size(), is(3));
    }

    @Test
    public void givenNoDir__whenGetRessources__thenThrowRessourceNotFound() throws Exception {
        thrown.expect(RessourceNotFoundException.class);
        fs.getRessources("g", "m", "v", "c2");
    }

    @Test
    public void givenNoDir__whenGetClassifiers__thenThrowRessourceNotFound() throws Exception {
        thrown.expect(RessourceNotFoundException.class);
        fs.getClassifiers("g", "m", "v2");
    }

    @Test
    public void givenNoDir__whenGetVersions__thenThrowRessourceNotFound() throws Exception {
        thrown.expect(RessourceNotFoundException.class);
        fs.getVersions("g", "m2");
    }

    @Test
    public void givenNoDir__whenGetModules__thenThrowRessourceNotFound() throws Exception {
        thrown.expect(RessourceNotFoundException.class);
        fs.getModules("g2");
    }

    @Test
    public void givenZip2Files__then2Files() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is;
        String group = "group1";
        String module = "module1";
        String version = "v1";
        String classifier = "c";

        is = classLoader.getResourceAsStream("html2Files.zip");
        fs.addZipFileIn(is, RessourcesManager.buildPath(group, module, version, classifier));


        assertThat(fs.getRessources(group, module, version, classifier).size(), is(2));
    }

    @Test
    public void givenEmptyZip__then0Files() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("empty.zip");

        fs.addZipFileIn(is, RessourcesManager.buildPath("g", "m", "v", "c"));

        assertThat(fs.getRessources("g", "m", "v", "c").size(), is(0));
    }

    @Test
    public void givenZipWithManifestFile__then1Files() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("Manifest.zip");

        fs.addZipFileIn(is, RessourcesManager.buildPath("g", "m", "v", "c"));

        assertThat(fs.getRessources("g", "m", "v", "c").size(), is(1));
    }
}
