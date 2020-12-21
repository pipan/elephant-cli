package com.pipan.elephant.config;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

public class Config {
    private Map<String, Object> map;

    public Config(Map<String, Object> map)
    {
        this.map = map;
    }

    public Config()
    {
        this(new Hashtable<>());
    }

    public static Config fromJson(JSONObject json)
    {
        Map<String, Object> map = new Hashtable<>();
        Iterator<String> i = json.keys();
        while (i.hasNext()) {
            String key = i.next();
            map.put(key, json.get(key));
        }

        return new Config(map);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name, T def)
    {
        if (!this.map.containsKey(name)) {
            return def;
        }
        return (T) this.map.get(name);
    }

    public String getString(String name, String def)
    {
        return this.get(name, def);
    }

    public Integer getInteger(String name, Integer def)
    {
        return this.get(name, def);
    }

    public Boolean getBoolean(String name, Boolean def)
    {
        return this.get(name, def);
    }

    public String getString(String name)
    {
        return this.get(name, null);
    }

    public boolean isEmpty()
    {
        return this.map.isEmpty();
    }
}
