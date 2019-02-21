package io.flexio.services.api.documentation.ResourcesManager;

import io.flexio.services.api.documentation.Exceptions.VersionNotRecognizedException;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class VersionExtractorTest {
    @Test
    public void nominal() throws Exception {
        VersionExtractor ve = new VersionExtractor("1.0.0-SNAPSHOOT");
        ve.parse();
        assertThat(ve.getMajor(), is(1));
        assertThat(ve.getMinor(), is(0));
        assertThat(ve.getPatch(), is(0));
        assertThat(ve.isSnapshot(), is(true));
    }

    @Test
    public void threeDigits() throws Exception {
        VersionExtractor ve = new VersionExtractor("1.2.2");
        ve.parse();
        assertThat(ve.getMajor(), is(1));
        assertThat(ve.getMinor(), is(2));
        assertThat(ve.getPatch(), is(2));
        assertThat(ve.isSnapshot(), is(false));
    }

    @Test
    public void twoDigits() throws Exception {
        VersionExtractor ve = new VersionExtractor("1.2");
        ve.parse();
        assertThat(ve.getMajor(), is(1));
        assertThat(ve.getMinor(), is(2));
        assertThat(ve.getPatch(), is(0));
        assertThat(ve.isSnapshot(), is(false));
    }

    @Test
    public void oneDigit() throws Exception {
        VersionExtractor ve = new VersionExtractor("1");
        ve.parse();
        assertThat(ve.getMajor(), is(1));
        assertThat(ve.getMinor(), is(0));
        assertThat(ve.getPatch(), is(0));
        assertThat(ve.isSnapshot(), is(false));
    }

    @Test
    public void misc() throws Exception {
        VersionExtractor ve = new VersionExtractor("512");
        ve.parse();
        assertThat(ve.getMajor(), is(512));
        assertThat(ve.getMinor(), is(0));
        assertThat(ve.getPatch(), is(0));
        assertThat(ve.isSnapshot(), is(false));

        ve = new VersionExtractor("15651.561615.15616517");
        ve.parse();
        assertThat(ve.getMajor(), is(15651));
        assertThat(ve.getMinor(), is(561615));
        assertThat(ve.getPatch(), is(15616517));
        assertThat(ve.isSnapshot(), is(false));
    }

    @Test
    public void oneDigitWithSnapshot() throws Exception {
        VersionExtractor ve = new VersionExtractor("5-kjhvbjvkjv");
        ve.parse();
        assertThat(ve.getMajor(), is(5));
        assertThat(ve.getMinor(), is(0));
        assertThat(ve.getPatch(), is(0));
        assertThat(ve.isSnapshot(), is(true));
    }

    @Test
    public void twoDigitWithSnapshot() throws Exception {
        VersionExtractor ve = new VersionExtractor("15.1-dev");
        ve.parse();
        assertThat(ve.getMajor(), is(15));
        assertThat(ve.getMinor(), is(1));
        assertThat(ve.getPatch(), is(0));
        assertThat(ve.isSnapshot(), is(true));
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
    public void compareVersionWithSnapshot() throws Exception {
        VersionExtractor ve1 = new VersionExtractor("5.0.0-dev");
        ve1.parse();

        VersionExtractor ve2 = new VersionExtractor("147.0.0-snapshot");
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