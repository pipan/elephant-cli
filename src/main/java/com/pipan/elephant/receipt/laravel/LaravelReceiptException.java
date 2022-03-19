package com.pipan.elephant.receipt.laravel;

public class LaravelReceiptException extends Exception {
    private static final long serialVersionUID = -2874993154198233450L;

    public LaravelReceiptException(String message) {
        super(message);
    }

    public LaravelReceiptException(String message, Throwable cause) {
        super(message, cause);
    }
}
