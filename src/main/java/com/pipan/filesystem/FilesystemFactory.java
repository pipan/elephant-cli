package com.pipan.filesystem;

public interface FilesystemFactory {
    public Filesystem create(String path);
}
