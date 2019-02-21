package io.flexio.services.api.documentation.Exceptions;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String s) {
        super(s);
    }

    public ResourceNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
