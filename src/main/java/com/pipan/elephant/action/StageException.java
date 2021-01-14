package com.pipan.elephant.action;

public class StageException extends Exception {
    private static final long serialVersionUID = -2874993154198233450L;

    public StageException(String message) {
        super(message);
    }

    public StageException(String message, Throwable cause) {
        super(message, cause);
    }
}
