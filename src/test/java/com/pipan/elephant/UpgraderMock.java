package com.pipan.elephant;

import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.source.Upgrader;
import com.pipan.filesystem.Directory;

public class UpgraderMock implements Upgrader {
    private Directory upgradeDir;
    private ElephantConfig upgradeConfig;

    @Override
    public boolean upgrade(Directory dir, ElephantConfig config) {
        this.upgradeConfig = config;
        this.upgradeDir = dir;
        dir.make();
        return true;
    }

    public Directory getDir() {
        return this.upgradeDir;
    }

    public ElephantConfig getConfig() {
        return this.upgradeConfig;
    }
}
