package com.pipan.elephant.workingdir;

import java.util.Map;
import java.util.Hashtable;

import com.pipan.filesystem.Directory;
import com.pipan.filesystem.File;
import com.pipan.filesystem.Filesystem;
import com.pipan.filesystem.SymbolicLink;

import org.json.JSONObject;

public class SimpleWorkingDirectory implements WorkingDirectory {
    private Filesystem filesystem;
    private Map<String, String> map;

    public SimpleWorkingDirectory(Filesystem filesystem, Map<String, String> map) {
        this.filesystem = filesystem;
        this.map = map;
    }

    public SimpleWorkingDirectory(Filesystem filesystem, JSONObject json) {
        this.filesystem = filesystem;
        this.map = new Hashtable();

        this.map.put("elephant", json.getString("elephant"));
        this.map.put("releases", json.getString("releases"));
        this.map.put("public", json.getString("public"));
        this.map.put("production_link", json.getString("production_link"));
        this.map.put("stage_link", json.getString("stage_link"));
    }

    @Override
    public Filesystem getFilesystem() {
        return this.filesystem;
    }

    @Override
    public File getConfigFile() {
        return this.filesystem.getFile(this.map.get("elephant"));
    }

    @Override
    public SymbolicLink getProductionLink() {
        return this.filesystem.getSymbolicLink(this.map.get("production_link"));
    }

    @Override
    public Directory getPublicDirectory() {
        return this.filesystem.getDirectory(this.map.get("public"));
    }

    @Override
    public Directory getReleasesDirectory() {
        return this.filesystem.getDirectory(this.map.get("releases"));
    }

    @Override
    public SymbolicLink getStageLink() {
        return this.filesystem.getSymbolicLink(this.map.get("stage_link"));
    }
}
