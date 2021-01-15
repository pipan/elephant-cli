package com.pipan.elephant.config;

import org.json.JSONObject;

public class SimpleElephantConfig implements ElephantConfig {
    protected JSONObject source;
    protected String sourceType;
    protected Integer historyLimit;

    public SimpleElephantConfig(JSONObject json) {
        this.historyLimit = json.getInt("history_limit");
        this.source = json.getJSONObject("source");
        this.sourceType = this.source.getString("type");
    }

    public Integer getHistoryLimit() {
        return this.historyLimit;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public JSONObject getSource() {
        return this.source;
    }
}
