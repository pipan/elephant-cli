package com.pipan.filesystem;

public class ReadException extends Exception {
    private static final long serialVersionUID = -2874993154098233450L;

    public ReadException(String message) {
        super(message);
    }

    public ReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
