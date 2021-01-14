package com.pipan.filesystem;

import com.pipan.filesystem.File;
import com.pipan.filesystem.ReadException;
import com.pipan.filesystem.WriteException;

import org.json.JSONObject;
import org.json.JSONException;

public class FileMock implements File {
    private String content;
    private ReadException readException;

    public FileMock() {
        this(null);
    }

    public FileMock(String content) {
        this.content = content;
    }

    public FileMock withReadException(ReadException e) {
        this.readException = e;
        return this;
    }

    @Override
    public boolean exists() {
        return this.content != null;
    }

    @Override
    public void delete() {
        this.content = null;
    }

    @Override
    public String read() throws ReadException {
        if (this.readException != null) {
            throw this.readException;
        }
        return this.content;
    }

    // todo move to some kind of json reader
    @Override
    public JSONObject readJson() throws ReadException {
        try {
            return new JSONObject(this.read());
        } catch (JSONException ex) {
            throw new ReadException("Cannot read json file: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void write(String content) throws WriteException {
        this.content = content;
    }

    // todo move to some kind of json writer
    @Override
    public void writeJson(JSONObject content) throws WriteException {
        this.write(content.toString());
    }
}
