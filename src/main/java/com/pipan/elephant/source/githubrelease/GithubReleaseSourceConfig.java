package com.pipan.elephant.source.githubrelease;

import org.json.JSONObject;

public class GithubReleaseSourceConfig {
    private String repository = "";
    private String asset = "";

    public GithubReleaseSourceConfig(JSONObject json) {
        this.repository = json.getString("repository");
        this.asset = json.getString("asset");
    }

    public String getRepository() {
        return this.repository;
    }

    public String getAsset() {
        return this.asset;
    }
}