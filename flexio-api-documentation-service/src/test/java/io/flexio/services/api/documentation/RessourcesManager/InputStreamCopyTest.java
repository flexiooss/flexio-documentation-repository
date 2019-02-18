package io.flexio.services.api.documentation.RessourcesManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class InputStreamCopyTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder tmpFolderInputStream = new TemporaryFolder();

    @Test
    public void givenInputStream__thenCopyIsIdentic() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        assertNotNull(classLoader);
        InputStream is = classLoader.getResourceAsStream("html.zip");
        int nb = is.available();

        try (InputStreamCopy cis = new InputStreamCopy(is, tmpFolderInputStream.getRoot().getAbsolutePath())) {
            assertThat(nb, is(cis.getCopy().available()));
        }
    }
}