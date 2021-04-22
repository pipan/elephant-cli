package com.pipan.elephant.github;

public class StatusNotOkException extends Exception {
    private static final long serialVersionUID = 8330938125098120502L;

    public StatusNotOkException(String message) {
        super(message);
    }
}
