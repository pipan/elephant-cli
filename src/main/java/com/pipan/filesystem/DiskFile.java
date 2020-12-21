package com.pipan.filesystem;

import java.io.IOException;
import java.nio.file.Files;

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
    public String read() {
        try {
            byte[] encoded = Files.readAllBytes(this.file.toPath());
            return new String(encoded);
        } catch (IOException | SecurityException | OutOfMemoryError ex) {
            // TODO throw custom exception
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    public JSONObject readJson() {
        return new JSONObject(this.read());
    }

    @Override
    public void write(String content) {
        try {
            Files.write(this.file.toPath(), content.getBytes());
        } catch (IOException | IllegalArgumentException | SecurityException | UnsupportedOperationException ex) {
            ex.printStackTrace();
            // TODO throw custom exception
        }
    }

    @Override
    public void writeJson(JSONObject content) {
        this.write(content.toString());
    }
}
