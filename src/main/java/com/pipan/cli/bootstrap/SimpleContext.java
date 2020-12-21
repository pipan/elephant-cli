package com.pipan.cli.bootstrap;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pipan.cli.controller.Controller;
import com.pipan.cli.middleware.Middleware;

public class SimpleContext implements RouteContext, MiddlewareContext {
    private Map<String, Controller> routes;
    private List<Middleware> middlewares;

    public SimpleContext()
    {
        this.routes = new Hashtable<>();
        this.middlewares = new LinkedList<>();
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

    public Map<String, Controller> getRoutes()
    {
        return this.routes;
    }

    public List<Middleware> getMiddlewares()
    {
        return this.middlewares;
    }
}
