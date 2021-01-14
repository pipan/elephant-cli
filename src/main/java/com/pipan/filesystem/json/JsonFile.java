package com.pipan.filesystem.json;

import com.pipan.filesystem.File;
import com.pipan.filesystem.ReadException;
import com.pipan.filesystem.WriteException;

import org.json.JSONObject;
import org.json.JSONException;

public class JsonFile {
    protected File file;

    public JsonFile(File file) {
        this.file = file;
    }

    public JSONObject read() throws ReadException {
        try {
            return new JSONObject(this.file.read());
        } catch (JSONException ex) {
            throw new ReadException("Cannot read json file: " + ex.getMessage(), ex);
        }
    }

    public void write(JSONObject json) throws WriteException {
        this.file.write(json.toString(4));
    }
}
