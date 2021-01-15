package com.pipan.elephant.exceptionhandler;

import com.pipan.cli.exception.ExceptionHandler;
import com.pipan.elephant.shell.Shell;

public class PrintExceptionHandler implements ExceptionHandler {
    private Shell shell;

    public PrintExceptionHandler(Shell shell) {
        this.shell = shell;
    }

    public void handle(Exception ex) {
        this.shell.err(ex.getMessage());
    }
}
