package com.pipan.elephant.repository;

import java.util.Hashtable;
import java.util.Map;

public class MapRepository<T> implements Repository<T> {
    private Map<String, T> map;

    public MapRepository() {
        this.map = new Hashtable<>();
    }
    
    public T get(String name) {
        return this.get(name, null);
    }

    public T get(String name, T defaultItem) {
        if (!this.map.containsKey(name)) {
            return defaultItem;
        }
        return this.map.get(name);
    }

    public Repository<T> add(String name, T item) {
        this.map.put(name, item);
        return this;
    }
}
