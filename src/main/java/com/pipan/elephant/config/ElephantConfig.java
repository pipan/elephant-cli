package com.pipan.elephant.config;

import org.json.JSONObject;

public interface ElephantConfig {
    public Integer getHistoryLimit();
    public String getSourceType();
    public String getReceipt();
    public JSONObject getSource();
    public JSONObject getFpm();
}
