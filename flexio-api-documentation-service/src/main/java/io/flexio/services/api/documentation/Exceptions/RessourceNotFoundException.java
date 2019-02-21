package io.flexio.services.api.documentation.Exceptions;

public class RessourceNotFoundException extends Exception {
    public RessourceNotFoundException() {
        super();
    }

    public RessourceNotFoundException(String s) {
        super(s);
    }

    public RessourceNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
