package com.pipan.filesystem;

import org.json.JSONObject;

public interface File {
    public boolean exists();
    public String getName();
    public void delete();
    public String read() throws ReadException;
    public void write(String content) throws WriteException;
}
