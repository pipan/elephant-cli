package com.pipan.cli.bootstrap;

import com.pipan.cli.middleware.Middleware;

public interface MiddlewareContext {
    public MiddlewareContext addMiddleware(Middleware middleware);
}
