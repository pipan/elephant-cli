package com.pipan.elephant.source.composerproject;

import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.service.ComposerService;
import com.pipan.elephant.source.Upgrader;
import com.pipan.filesystem.Directory;

public class ComposerProjectUpgrader implements Upgrader {
    private ComposerService composer;
    private ConsoleOutput output;

    public ComposerProjectUpgrader(ComposerService composer) {
        this.composer = composer;
        this.output = new ConsoleOutput();
    }

    @Override
    public boolean upgrade(Directory dir, ElephantConfig config) throws Exception {
        ComposerProjectSourceConfig composerConfig = new ComposerProjectSourceConfig(config.getSource());

        this.output.write("[...] Installing composer project");
        try {
            this.composer.createProject(composerConfig.getPackageName(), dir.getAbsolutePath());
        } catch (Exception ex) {
            this.output.rewrite("[<red> x </red>] Installing composer project: <red>" + ex.getMessage() + "</red>");
            return false;
        }
        this.output.rewrite("[<green> ✔️ </green>] Installing composer project");
        return true;
    }
}
