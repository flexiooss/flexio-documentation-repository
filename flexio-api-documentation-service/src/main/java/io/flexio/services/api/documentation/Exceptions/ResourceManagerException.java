package io.flexio.services.api.documentation.Exceptions;

public class ResourceManagerException extends Exception {
    public ResourceManagerException() {
        super();
    }

    public ResourceManagerException(String s) {
        super(s);
    }

    public ResourceManagerException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
