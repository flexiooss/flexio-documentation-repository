package io.flexio.services.api.documentation.Exceptions;

public class RessourceNotFoundException extends Exception {
    public RessourceNotFoundException() {
    }

    public RessourceNotFoundException(String s) {
        super(s);
    }

    public RessourceNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
