package com.pipan.elephant.source.composerproject;

import org.json.JSONObject;

public class ComposerProjectSourceConfig {
    private String packageName;

    public ComposerProjectSourceConfig(JSONObject json) {
        this.packageName = json.getString("package");
        //todo throw custom exception
    }

    public String getPackageName() {
        return this.packageName;
    }
}