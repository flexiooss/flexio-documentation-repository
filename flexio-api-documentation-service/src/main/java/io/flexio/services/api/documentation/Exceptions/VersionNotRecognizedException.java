package io.flexio.services.api.documentation.Exceptions;

public class VersionNotRecognizedException extends  Exception{
    public VersionNotRecognizedException() {
        super();
    }

    public VersionNotRecognizedException(String s) {
        super(s);
    }

    public VersionNotRecognizedException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
