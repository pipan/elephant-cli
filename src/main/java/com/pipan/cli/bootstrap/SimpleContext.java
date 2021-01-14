package com.pipan.cli.bootstrap;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pipan.cli.controller.Controller;
import com.pipan.cli.exception.ExceptionHandler;
import com.pipan.cli.middleware.Middleware;

public class SimpleContext implements RouteContext, MiddlewareContext, ExceptionHandlerContext {
    private Map<String, Controller> routes;
    private List<Middleware> middlewares;
    private List<ExceptionHandler> exceptionHandlers;

    public SimpleContext()
    {
        this.routes = new Hashtable<>();
        this.middlewares = new LinkedList<>();
        this.exceptionHandlers = new LinkedList<>();
    }

    @Override
    public RouteContext addRoute(String name, Controller controller) {
        return this.addRoute(Arrays.asList(name), controller);
    }

    @Override
    public RouteContext addRoute(List<String> names, Controller controller) {
        for (String name : names) {
            this.routes.put(name, controller);
        }
        return this;
    }

    @Override
    public MiddlewareContext addMiddleware(Middleware middleware) {
        this.middlewares.add(middleware);
        return this;
    }

    @Override
    public ExceptionHandlerContext addExceptionHandler(ExceptionHandler handler) {
        this.exceptionHandlers.add(handler);
        return this;
    }

    public Map<String, Controller> getRoutes() {
        return this.routes;
    }

    public List<Middleware> getMiddlewares() {
        return this.middlewares;
    }

    public List<ExceptionHandler> getExceptionHandlers() {
        return this.exceptionHandlers;
    }
}
