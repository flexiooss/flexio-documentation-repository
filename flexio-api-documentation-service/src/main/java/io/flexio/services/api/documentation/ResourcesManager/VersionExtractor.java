package io.flexio.services.api.documentation.ResourcesManager;

import io.flexio.services.api.documentation.Exceptions.VersionNotRecognizedException;
import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionExtractor implements Comparable<VersionExtractor> {
    private static CategorizedLogger log = CategorizedLogger.getLogger(VersionExtractor.class);

    private String version;
    private boolean isSnapshot;
    private int major;
    private int minor;
    private int patch;

    public VersionExtractor(String version) {
        this.version = version;
    }

    public void parse() throws VersionNotRecognizedException {
        Pattern p = Pattern.compile("(\\d+)?\\.?(\\d+)?\\.?(\\*|\\d+)(\\-\\w*)?");
        Matcher m = p.matcher(this.version);
        if (m.matches()) {
            for (int i = 0; i <= m.groupCount(); i++)
                log.trace("group " + i + " : " + m.group(i));

            if (m.group(1) == null && m.group(2) == null && m.group(3) != null) {
                // ex : 1
                this.major = Integer.parseInt(m.group(3));
                this.minor = 0;
                this.patch = 0;
            } else if (m.group(1) != null && m.group(2) == null && m.group(3) != null) {
                // ex : 1.2
                this.major = Integer.parseInt(m.group(1));
                this.minor = Integer.parseInt(m.group(3));
                this.patch = 0;
            } else if (m.group(1) != null && m.group(2) != null && m.group(3) != null) {
                // ex : 1.2.3
                this.major = Integer.parseInt(m.group(1));
                this.minor = Integer.parseInt(m.group(2));
                this.patch = Integer.parseInt(m.group(3));
            } else {
                throw new VersionNotRecognizedException("No match");
            }


            this.isSnapshot = m.group(4) != null;

            log.info(this.version + " => version : " + this.prettyPrint() + " " + this.toString());
        } else {
            throw new VersionNotRecognizedException("No match");
        }
    }


    public boolean isSnapshot() {
        return isSnapshot;
    }

    @Override
    public int compareTo(VersionExtractor ve) {
        if (this.major > ve.getMajor()) {
            return 1;
        } else if (this.major == ve.getMajor()) {
            if (this.minor > ve.getMinor()) {
                return 1;
            } else if (this.minor == ve.getMinor()) {
                if (this.patch > ve.getPatch()) {
                    return 1;
                } else if (this.patch == ve.getPatch())
                    return 0;
            }
        }
        return -1;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public String toString() {
        return "VersionExtractor{" +
                " major=" + major +
                ", minor=" + minor +
                ", patch=" + patch +
                ", isSnapshot=" + isSnapshot +
                '}';
    }

    public String prettyPrint() {
        return this.major + "." + this.minor + "." + this.patch;
    }
}
