package com.pipan.cli.bootstrap;

import java.util.List;

import com.pipan.cli.controller.Controller;
import com.pipan.cli.exception.ExceptionHandler;

public interface ExceptionHandlerContext {
    public ExceptionHandlerContext addExceptionHandler(ExceptionHandler handler);
}
