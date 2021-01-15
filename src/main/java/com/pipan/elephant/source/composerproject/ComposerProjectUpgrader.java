package com.pipan.elephant.source.composerproject;

import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.service.ComposerService;
import com.pipan.elephant.source.Upgrader;
import com.pipan.filesystem.Directory;

public class ComposerProjectUpgrader implements Upgrader {
    private ComposerService composer;
    private Logger logger;

    public ComposerProjectUpgrader(ComposerService composer, Logger logger) {
        this.composer = composer;
        this.logger = logger;
    }

    @Override
    public boolean upgrade(Directory dir, ElephantConfig config) throws Exception {
        ComposerProjectSourceConfig composerConfig = new ComposerProjectSourceConfig(config.getSource());

        this.logger.info("Installing composer project with dependencies");
        return this.composer.createProject(composerConfig.getPackageName(), dir.getAbsolutePath());
    }
}
