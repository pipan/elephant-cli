package com.pipan.cli.routing;

import java.util.Map;

import com.pipan.cli.controller.Controller;

public class RouteTable {
    private Map<String, Controller> map;

    public RouteTable(Map<String, Controller> routes)
    {
        this.map = routes;
    }

    public Controller getController(String key)
    {
        return this.map.get(key);
    }
}
