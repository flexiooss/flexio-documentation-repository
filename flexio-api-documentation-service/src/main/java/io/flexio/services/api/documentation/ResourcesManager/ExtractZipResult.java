package io.flexio.services.api.documentation.ResourcesManager;

public class ExtractZipResult {
    private boolean extracted;
    private String path;

    public ExtractZipResult(boolean extracted, String path) {
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
