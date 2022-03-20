package com.pipan.elephant.config;

import java.util.Iterator;

import org.json.JSONObject;

public class OverrideElephantConfig implements ElephantConfig {
    protected ElephantConfig config;
    protected ElephantConfig fallbackConfig;

    public OverrideElephantConfig(JSONObject json, ElephantConfig fallbackConfig) {
        this.config = new SimpleElephantConfig(json);
        this.fallbackConfig = fallbackConfig;
    }

    public Integer getHistoryLimit() {
        if (this.config.getHistoryLimit() == null) {
            return this.fallbackConfig.getHistoryLimit();
        }
        return this.config.getHistoryLimit();
    }

    public String getSourceType() {
        if (this.config.getSourceType() == null) {
            return this.fallbackConfig.getSourceType();
        }
        return this.config.getSourceType();
    }

    public String getReceipt() {
        if (this.config.getReceipt() == null) {
            return this.fallbackConfig.getReceipt();
        }
        return this.config.getReceipt();
    }

    public JSONObject getSource() {
        if (this.config.getSource() == null) {
            return this.fallbackConfig.getSource();
        }
        return this.config.getSource();
    }

    public JSONObject getFpm() {
        JSONObject fallbackJson = this.fallbackConfig.getFpm();
        JSONObject clone = new JSONObject(fallbackJson, JSONObject.getNames(fallbackJson));
        if (this.config.getFpm() != null) {
            JSONObject json = this.config.getFpm();
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                clone.put(key, json.get(key));
            }
        }
        
        return clone;
    }

    public Boolean getNginx() {
        if (this.config.getNginx() == null) {
            return this.fallbackConfig.getNginx();
        }
        return this.config.getNginx();
    }
}
