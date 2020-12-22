package com.pipan.filesystem;

public class WriteException extends Exception {
    private static final long serialVersionUID = -2874993154098233450L;

    public WriteException(String message) {
        super(message);
    }

    public WriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
