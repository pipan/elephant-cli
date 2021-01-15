package com.pipan.filesystem;

import java.io.IOException;
import java.nio.file.Files;

import org.json.JSONException;
import org.json.JSONObject;

public class DiskFile implements File {
    private java.io.File file;

    public DiskFile(java.io.File file) {
        this.file = file;
    }

    @Override
    public boolean exists() {
        return this.file.exists() && this.file.isFile();
    }

    @Override
    public void delete() {
        if (!this.exists()) {
            return;
        }
        this.file.delete();
    }

    @Override
    public String read() throws ReadException {
        try {
            byte[] encoded = Files.readAllBytes(this.file.toPath());
            return new String(encoded);
        } catch (IOException | SecurityException | OutOfMemoryError ex) {
            throw new ReadException("Cannot read file: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void write(String content) throws WriteException {
        try {
            Files.write(this.file.toPath(), content.getBytes());
        } catch (IOException | IllegalArgumentException | SecurityException | UnsupportedOperationException ex) {
            throw new WriteException("Cannot write to file: " + ex.getMessage(), ex);
        }
    }
}
