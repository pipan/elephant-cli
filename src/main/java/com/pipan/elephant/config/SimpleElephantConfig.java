package com.pipan.elephant.config;

import org.json.JSONObject;

public class SimpleElephantConfig implements ElephantConfig {
    protected JSONObject source;
    protected JSONObject fpm;
    protected String sourceType;
    protected Integer historyLimit;
    protected String receipt;

    public SimpleElephantConfig(JSONObject json) {
        this.historyLimit = json.getInt("history_limit");
        this.source = json.getJSONObject("source");
        this.sourceType = this.source.getString("type");
        if (json.has("receipt")) {
            this.receipt = json.getString("receipt");
        }
        if (json.has("fpm")) {
            Object fpmObject = json.get("fpm");
            if (fpmObject instanceof JSONObject) {
                this.fpm = json.getJSONObject("fpm");
            } else if (fpmObject instanceof Boolean) {
                this.fpm = new JSONObject();
                this.fpm.put("command", "");
                this.fpm.put("version", "");
            }
            
        }
    }

    public Integer getHistoryLimit() {
        return this.historyLimit;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public String getReceipt() {
        return this.receipt;
    }

    public JSONObject getSource() {
        return this.source;
    }

    public JSONObject getFpm() {
        return this.fpm;
    }
}
