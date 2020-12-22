package com.pipan.filesystem;

import org.json.JSONObject;

public interface File {
    public boolean exists();
    public String read() throws ReadException;
    public JSONObject readJson() throws ReadException;
    public void write(String content) throws WriteException;
    public void writeJson(JSONObject content) throws WriteException;
}
