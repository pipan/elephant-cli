package com.pipan.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiskSymbolicLink implements SymbolicLink {
    private File file;

    public DiskSymbolicLink(File file) {
        this.file = file;
    }

    protected Path getTargetPath() {
        try {
            return this.file.toPath().toRealPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean exists() {
        return this.file.exists();
    }

    @Override
    public void delete() {
        if (!this.exists()) {
            return;
        }
        this.file.delete();
    }
    
    @Override
    public Directory getTargetDirectory() {
        if (!this.exists()) {
            return null;
        }
        Path path = this.getTargetPath();
        if (path == null) {
            return null;
        }
        return new DiskDirectory(path.toFile());
    }

    @Override
    public String getTargetString() {
        if (!this.exists()) {
            return null;
        }
        Path path = this.getTargetPath();
        if (path == null) {
            return null;
        }
        return path.toString();
    }

    @Override
    public void setTarget(String target) {
        Path targetPath = Paths.get(target);
        try {
            if (this.file.exists()) {
                this.file.delete();
            }
            Files.createSymbolicLink(this.file.toPath(), targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
