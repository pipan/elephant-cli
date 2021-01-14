package com.pipan.filesystem;

import java.nio.file.Paths;

public class DiskFilesystemFactory implements FilesystemFactory {
    public Filesystem create(String path) {
        return new DiskFilesystem(Paths.get(path));
    }
}
