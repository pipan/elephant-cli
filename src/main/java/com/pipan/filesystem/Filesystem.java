package com.pipan.filesystem;

public interface Filesystem {
    public Filesystem withBase(String path);
    public File getFile(String name);
    public Directory getDirectory(String name);
    public SymbolicLink getSymbolicLink(String name);
}
