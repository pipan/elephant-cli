package com.pipan.filesystem;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiskFilesystem implements Filesystem {
    private Path base;

    public DiskFilesystem(Path base)
    {
        this.base = base;
    }

    protected File getIoFile(String name) {
        return new File(this.base.toString(), name);
    }

    @Override
    public Filesystem withBase(String path) {
        return new DiskFilesystem(Paths.get(path));
    }

    @Override
    public Directory getDirectory(String name) {
        return new DiskDirectory(this.getIoFile(name));
    }

    @Override
    public com.pipan.filesystem.File getFile(String name) {
        return new DiskFile(this.getIoFile(name));
    }

    @Override
    public SymbolicLink getSymbolicLink(String name) {
        return new DiskSymbolicLink(this.getIoFile(name));
    }
}
