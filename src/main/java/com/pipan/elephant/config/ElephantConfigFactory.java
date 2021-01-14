package com.pipan.elephant.config;

import com.pipan.elephant.Resource;
import com.pipan.filesystem.File;
import com.pipan.filesystem.ReadException;
import com.pipan.filesystem.json.JsonFile;

import org.json.JSONObject;

public class ElephantConfigFactory {
    public ElephantConfig create(File configFile) throws Exception, ReadException {
        ElephantConfig fallbackConfig = new SimpleElephantConfig(new JSONObject(
            Resource.getContent("fallback/elephant.json")
        ));

        JsonFile jsonFile = new JsonFile(configFile);
        return new OverrideElephantConfig(jsonFile.read(), fallbackConfig);
    }
}
