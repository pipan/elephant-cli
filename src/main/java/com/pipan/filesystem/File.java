package com.pipan.filesystem;

import org.json.JSONObject;

public interface File {
    public boolean exists();
    public void delete();
    public String read() throws ReadException;
    @Deprecated
    public JSONObject readJson() throws ReadException;
    public void write(String content) throws WriteException;
    @Deprecated
    public void writeJson(JSONObject content) throws WriteException;
}
