package com.pipan.elephant.source;

import com.pipan.elephant.config.Config;
import com.pipan.elephant.service.ComposerService;
import com.pipan.filesystem.Directory;

public class ComposerProjectUpgrader implements Upgrader {
    private ComposerService composer;

    public ComposerProjectUpgrader(ComposerService composer) {
        this.composer = composer;
    }

    @Override
    public boolean upgrade(Directory dir, Config config) throws Exception {
        String packageName = config.getString("package");
        if (packageName == null || packageName.isEmpty()) {
            throw new Exception("Composer project package name is empty");
        }
        System.out.println("[ INFO ] Installing composer project with dependencies");
        return this.composer.createProject(packageName, dir.getAbsolutePath());
    }
}
