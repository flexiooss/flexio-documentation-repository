package io.flexio.services.api.documentation.RessourcesManager;

import io.flexio.services.api.documentation.Exceptions.VersionNotRecognizedException;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class VersionExtractorTest {
    @Test
    public void nominal() throws Exception {
        VersionExtractor ve = new VersionExtractor("1.0.0-SNAPSHOOT");
        ve.parse();
        assertThat(ve.getMajor(), is(1));
        assertThat(ve.getMinor(), is(0));
        assertThat(ve.getPatch(), is(0));
        assertThat(ve.isSnapshoot(), is(true));
    }

    @Test
    public void threeDigits() throws Exception {
        VersionExtractor ve = new VersionExtractor("1.2.2");
        ve.parse();
        assertThat(ve.getMajor(), is(1));
        assertThat(ve.getMinor(), is(2));
        assertThat(ve.getPatch(), is(2));
        assertThat(ve.isSnapshoot(), is(false));
    }

    @Test
    public void twoDigits() throws Exception {
        VersionExtractor ve = new VersionExtractor("1.2");
        ve.parse();
        assertThat(ve.getMajor(), is(1));
        assertThat(ve.getMinor(), is(2));
        assertThat(ve.getPatch(), is(0));
        assertThat(ve.isSnapshoot(), is(false));
    }

    @Test
    public void Onedigit() throws Exception {
        VersionExtractor ve = new VersionExtractor("1");
        ve.parse();
        assertThat(ve.getMajor(), is(1));
        assertThat(ve.getMinor(), is(0));
        assertThat(ve.getPatch(), is(0));
        assertThat(ve.isSnapshoot(), is(false));
    }

    @Test
    public void oneDigitWithSnapshoot() throws Exception {
        VersionExtractor ve = new VersionExtractor("5-dev");
        ve.parse();
        assertThat(ve.getMajor(), is(5));
        assertThat(ve.getMinor(), is(0));
        assertThat(ve.getPatch(), is(0));
        assertThat(ve.isSnapshoot(), is(true));
    }

    @Test
    public void twoDigitWithSnapshoot() throws Exception {
        VersionExtractor ve = new VersionExtractor("15.1-dev");
        ve.parse();
        assertThat(ve.getMajor(), is(15));
        assertThat(ve.getMinor(), is(1));
        assertThat(ve.getPatch(), is(0));
        assertThat(ve.isSnapshoot(), is(true));
    }

    @Test(expected = VersionNotRecognizedException.class)
    public void errorWithDev() throws Exception {
        VersionExtractor ve = new VersionExtractor("plok-dev");
        ve.parse();
    }

    @Test(expected = VersionNotRecognizedException.class)
    public void error() throws Exception {
        VersionExtractor ve = new VersionExtractor("plok");
        ve.parse();
    }
}