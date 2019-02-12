package io.flexio.services.api.documentation.Exceptions;

public class DirectoryNotExistsException extends Exception {
    public DirectoryNotExistsException() {
    }

    public DirectoryNotExistsException(String s) {
        super(s);
    }

    public DirectoryNotExistsException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
