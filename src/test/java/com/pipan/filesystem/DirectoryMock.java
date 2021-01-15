package com.pipan.filesystem;

import java.io.File;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import com.pipan.filesystem.Directory;

import org.junit.jupiter.api.Assertions;

public class DirectoryMock implements Directory {
    private boolean status;
    private String path;
    private SortedMap<String, DirectoryMock> childrens;

    public DirectoryMock(String path) {
        this(path, false);
    }

    public DirectoryMock(String path, boolean status) {
        this.path = path;
        this.status = status;
        this.childrens = new TreeMap<>();
    }

    public void assertExists() throws Exception {
        Assertions.assertTrue(this.exists(), "Directory should exist");
    }

    public void assertChildExists(String dir) throws Exception {
        Assertions.assertTrue(this.enterDir(dir).exists(), "Directory should contain child " + dir);
    }

    public void assertChildMissing(String dir) throws Exception {
        Assertions.assertFalse(this.enterDir(dir).exists(), "Directory should not contain child " + dir);
    }

    public DirectoryMock withChild(String name, DirectoryMock dir) {
        this.childrens.put(name, dir);
        return this;
    }

    public DirectoryMock withChild(String name) {
        this.childrens.put(name, new DirectoryMock(this.path + File.separator + name, true));
        return this;
    }

    @Override
    public boolean exists() {
        return this.status;
    }

    @Override
    public String getAbsolutePath() {
        return this.path;
    }

    @Override
    public String getName() {
        String[] split = this.path.split(File.separator);
        return split[split.length - 1];
    }

    @Override
    public void make() {
        this.status = true;
    }

    @Override
    public Collection<? extends Directory> getDirectoryList() {
        return this.childrens.values();
    }

    @Override
    public void delete() {
        this.status = false;
    }

    @Override
    public Directory enterDir(String name) {
        if (!this.childrens.containsKey(name)) {
            this.childrens.put(name, new DirectoryMock(this.path + File.separator + name));
        }
        return this.childrens.get(name);
    }

    @Override
    public Directory leaveDir() {
        return null;
    }
}
