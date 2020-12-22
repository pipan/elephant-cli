package com.pipan.elephant;

import com.pipan.filesystem.File;
import com.pipan.filesystem.ReadException;
import com.pipan.filesystem.WriteException;

import org.json.JSONException;
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
    public String read() throws ReadException {
        return this.content;
    }

    @Override
    public JSONObject readJson() throws ReadException {
        try {
            return new JSONObject(this.content);
        } catch (JSONException ex) {
            throw new ReadException("Mock json exception", ex);
        }
        
    }

    @Override
    public void write(String content) throws WriteException {
        this.content = content;
    }

    @Override
    public void writeJson(JSONObject content) throws WriteException {
        this.write(content.toString());
    }
}
