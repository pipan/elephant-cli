package com.pipan.elephant.source;

import java.util.Hashtable;
import java.util.Map;

public class UpgraderRepository {
    private Map<String, Upgrader> map;

    public UpgraderRepository()
    {
        this.map = new Hashtable<>();
    }
    
    public Upgrader get(String name)
    {
        return this.map.get(name);
    }

    public UpgraderRepository add(String name, Upgrader upgrader)
    {
        this.map.put(name, upgrader);
        return this;
    }
}
