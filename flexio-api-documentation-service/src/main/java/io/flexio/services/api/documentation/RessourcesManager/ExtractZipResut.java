package io.flexio.services.api.documentation.RessourcesManager;

public class ExtractZipResut {
    private boolean extracted;
    private String path;

    public ExtractZipResut(boolean extracted, String path) {
        this.extracted = extracted;
        this.path = path;
    }

    public boolean isExtracted() {
        return extracted;
    }

    public String getPath() {
        return path;
    }
}
