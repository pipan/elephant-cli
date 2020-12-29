package com.pipan.cli.exception;

public class NotFoundException extends Exception {
    private static final long serialVersionUID = 8330938125098120501L;

    public NotFoundException(String message) {
        super(message);
    }
}
