package io.flexio.services.api.documentation.RessourcesManager;

import io.flexio.services.api.documentation.Exceptions.VersionNotRecognizedException;
import io.flexio.services.api.documentation.api.types.Version;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.*;
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

    @Test
    public void comparePatchVersion() throws Exception {
        VersionExtractor ve1 = new VersionExtractor("0.0.1");
        ve1.parse();

        VersionExtractor ve2 = new VersionExtractor("0.0.2");
        ve2.parse();

        assertThat(ve1.compareTo(ve2), lessThan(0));
        assertThat(ve2.compareTo(ve1), greaterThan(0));

        VersionExtractor ve3 = new VersionExtractor("0.0.2");
        ve3.parse();

        assertThat(ve2.compareTo(ve3), is(0));
    }

    @Test
    public void compareMinorVersion() throws Exception {
        VersionExtractor ve1 = new VersionExtractor("0.1.0");
        ve1.parse();

        VersionExtractor ve2 = new VersionExtractor("0.2.0");
        ve2.parse();

        assertThat(ve1.compareTo(ve2), lessThan(0));
        assertThat(ve2.compareTo(ve1), greaterThan(0));

        VersionExtractor ve3 = new VersionExtractor("0.1.1000");
        ve3.parse();

        assertThat(ve1.compareTo(ve3), lessThan(0));
        assertThat(ve2.compareTo(ve3), greaterThan(0));

        VersionExtractor ve4 = new VersionExtractor("0.2.0");
        ve4.parse();

        assertThat(ve2.compareTo(ve4), is(0));
    }

    @Test
    public void compareMajorVersion() throws Exception {
        VersionExtractor ve1 = new VersionExtractor("1.0.0");
        ve1.parse();

        VersionExtractor ve2 = new VersionExtractor("2.0.0");
        ve2.parse();

        assertThat(ve1.compareTo(ve2), lessThan(0));
        assertThat(ve2.compareTo(ve1), greaterThan(0));

        VersionExtractor ve3 = new VersionExtractor("1.48.50");
        ve3.parse();

        assertThat(ve1.compareTo(ve3), lessThan(0));
        assertThat(ve2.compareTo(ve3), greaterThan(0));
        assertThat(ve1.compareTo(ve3), lessThan(0));

        VersionExtractor ve4 = new VersionExtractor("2.0.0");
        ve4.parse();

        assertThat(ve2.compareTo(ve4), is(0));
    }

    @Test
    public void compareVersionWithSnapshoot() throws Exception {
        VersionExtractor ve1 = new VersionExtractor("5.0.0-dev");
        ve1.parse();

        VersionExtractor ve2 = new VersionExtractor("147.0.0-snapshoot");
        ve2.parse();

        assertThat(ve1.compareTo(ve2), lessThan(0));
        assertThat(ve2.compareTo(ve1), greaterThan(0));

        VersionExtractor ve3 = new VersionExtractor("1.48.50");
        ve3.parse();

        assertThat(ve1.compareTo(ve3), greaterThan(0));
        assertThat(ve2.compareTo(ve3), greaterThan(0));
        assertThat(ve3.compareTo(ve1), lessThan(0));

        VersionExtractor ve4 = new VersionExtractor("5.0.0");
        ve4.parse();

        assertThat(ve1.compareTo(ve4), is(0));
    }


}