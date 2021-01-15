package com.pipan.elephant.source.git;

import org.json.JSONObject;

public class GitSourceConfig {
    private String url;
    private String branch = "";
    private Boolean composer = false;

    public GitSourceConfig(JSONObject json) {
        this.url = json.getString("url");
        //todo throw custom exception
        if (json.has("composer")) {
            this.composer = json.getBoolean("composer");
        }
        if (json.has("branch")) {
            this.branch = json.getString("branch");
        }
    }

    public String getUrl() {
        return this.url;
    }

    public Boolean hasComposer() {
        return this.composer;
    }

    public String getBranch() {
        return this.branch;
    }
}