package com.pipan.elephant;

import com.pipan.filesystem.File;

import org.json.JSONObject;

public class FileMock implements File {
    private String content;

    public FileMock() {
        this(null);
    }

    public FileMock(String content) {
        this.content = content;
    }

    @Override
    public boolean exists() {
        return this.content != null;
    }

    @Override
    public String read() {
        return this.content;
    }

    @Override
    public JSONObject readJson() {
        return new JSONObject(this.content);
    }

    @Override
    public void write(String content) {
        this.content = content;
    }

    @Override
    public void writeJson(JSONObject content) {
        this.write(content.toString());
    }
}
