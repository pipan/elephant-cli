package com.pipan.filesystem;

import org.json.JSONObject;

public interface File {
    public boolean exists();
    public String read();
    public JSONObject readJson();
    public void write(String content);
    public void writeJson(JSONObject content);
}
