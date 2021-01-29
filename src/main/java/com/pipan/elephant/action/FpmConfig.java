package com.pipan.elephant.action;

import org.json.JSONObject;

public class FpmConfig {
    private String command;
    private String version = "";

    public FpmConfig(JSONObject json) {
        this.command = json.getString("command");
        this.version = json.getString("version");
    }

    public Boolean isEnabled() {
        return !this.command.isEmpty() && !this.command.equals("none");
    }

    public String getCommand() {
        return this.command;
    }

    public String getVersion() {
        return this.version;
    }
}