package com.pipan.elephant;

import com.pipan.elephant.config.Config;
import com.pipan.elephant.source.Upgrader;
import com.pipan.filesystem.Directory;

public class UpgraderMock implements Upgrader {
    private Directory upgradeDir;
    private Config upgradeConfig;

    @Override
    public boolean upgrade(Directory dir, Config config) {
        this.upgradeConfig = config;
        this.upgradeDir = dir;
        dir.make();
        return true;
    }

    public Directory getDir() {
        return this.upgradeDir;
    }

    public Config getConfig() {
        return this.upgradeConfig;
    }
}
