package com.pipan.cli.bootstrap;

import java.util.List;

import com.pipan.cli.controller.Controller;

public interface RouteContext {
    public RouteContext addRoute(String name, Controller controller);
    public RouteContext addRoute(List<String> names, Controller controller);
}
