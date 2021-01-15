package com.pipan.filesystem;

public class FilesystemMockFactory implements FilesystemFactory {
    private Filesystem filesystem;

    public FilesystemMockFactory(Filesystem filesystem) {
        this.filesystem = filesystem;
    }

    public Filesystem create(String path) {
        return filesystem;
    }
}