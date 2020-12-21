package com.pipan.filesystem;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class DiskDirectory implements Directory {
    public File file;

    public DiskDirectory(File file) {
        this.file = file;
    }

    @Override
    public boolean exists() {
        return this.file.exists() && this.file.isDirectory();
    }

    @Override
    public String getAbsolutePath() {
        return this.file.getAbsolutePath();
    }

    @Override
    public String getName() {
        return this.file.getName();
    }

    @Override
    public Collection<? extends Directory> getDirectoryList() {
        File[] files = this.file.listFiles(new FileFilter(){
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        List<Directory> dirs = new LinkedList<>();
        for (File file : files) {
            dirs.add(new DiskDirectory(file));
        }
        return dirs;
    }

    @Override
    public void delete() {
        if (!this.file.exists()) {
            return;
        }
        try {
            Files.walk(this.file.toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void make() {
        this.file.mkdir();
    }

    @Override
    public Directory enterDir(String name) {
        return new DiskDirectory(new File(this.file, name));
    }

    @Override
    public Directory leaveDir() {
        return new DiskDirectory(this.file.getParentFile());
    }
}
