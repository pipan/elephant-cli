package com.pipan.elephant.repository;

public interface Repository<T> {
    public T get(String key);
    public T get(String key, T defaultItem);
    public Repository<T> add(String key, T item);
}