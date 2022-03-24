package com.pipan.filesystem;

import java.util.Hashtable;
import java.util.Map;

import com.pipan.filesystem.Directory;
import com.pipan.filesystem.File;
import com.pipan.filesystem.Filesystem;
import com.pipan.filesystem.SymbolicLink;

import org.junit.jupiter.api.Assertions;

public class FilesystemMock implements Filesystem {
    private Map<String, File> files;
    private Map<String, Directory> dirs;
    private Map<String, SymbolicLink> symLinks;
    private Map<String, Filesystem> subsystems;
    private String base;

    public FilesystemMock(String base) {
        this.base = base;
        this.files = new Hashtable<>();
        this.dirs = new Hashtable<>();
        this.symLinks = new Hashtable<>();
        this.subsystems = new Hashtable<>();
    }

    public FilesystemMock() {
        this("");
    }

    public void assertFileExists(String name) {
        Assertions.assertTrue(this.getFile(name).exists());
    }

    public void assertFileContent(String name, String content) throws Exception {
        Assertions.assertEquals(content, this.getFile(name).read(), "File content is different");
    }

    public FilesystemMock withFile(File file) {
        this.files.put(file.getName(), file);
        return this;
    }

    public FilesystemMock withFile(String name, String content) {
        return this.withFile(new FileMock(this.base, name, content));
    }

    public FilesystemMock withFile(String name) {
        return this.withFile(name, null);
    }

    public FilesystemMock withDir(String name, Directory dir) {
        this.dirs.put(name, dir);
        return this;
    }

    public FilesystemMock withSubsystem(String name, Filesystem subsystem) {
        this.subsystems.put(name, subsystem);
        return this;
    }

    public FilesystemMock withSymbolicLink(String name, SymbolicLink symlink) {
        this.symLinks.put(name, symlink);
        return this;
    }

    @Override
    public String getBase() {
        return this.base;
    }

    @Override
    public Directory getDirectory(String name) {
        String[] dirs = name.split(java.io.File.separator);
        Directory directory = this.dirs.get(dirs[0]);
        for (Integer i = 1; i < dirs.length; i++) {
            directory = directory.enterDir(dirs[i]);
        }
        return directory;
    }

    @Override
    public File getFile(String name) {
        return this.files.get(name);
    }

    @Override
    public SymbolicLink getSymbolicLink(String name) {
        return this.symLinks.get(name);
    }

    @Override
    public Filesystem withBase(String path) {
        return this.subsystems.get(path);
    }
}
