package com.pipan.elephant.source.git;

import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.service.ComposerService;
import com.pipan.elephant.service.GitService;
import com.pipan.elephant.source.Upgrader;
import com.pipan.filesystem.Directory;

public class GitUpgrader implements Upgrader {
    private GitService git;
    private ComposerService composer;
    private ConsoleOutput output;

    public GitUpgrader(GitService git, ComposerService composer) {
        this.git = git;
        this.composer = composer;
        this.output = new ConsoleOutput();
    }

    public boolean upgrade(Directory dir, ElephantConfig config) throws Exception {    
        GitSourceConfig gitConfig = new GitSourceConfig(config.getSource());
        this.output.write("[...] Cloning repository");
        try {
            this.git.clone(gitConfig.getUrl(), dir.getAbsolutePath());
        } catch (Exception ex) {
            this.output.rewrite("[<red> x </red>] Cloning repository: <red>" + ex.getMessage() + "</red>");
            return false;
        }
        this.output.rewrite("[<green> ✔️ </green>] Cloning repository");
        
        // todo checkout specific branch

        if (gitConfig.hasComposer()) {
            this.output.write("[...] Installing composer dependencies");
            try {
                this.composer.install(dir.getAbsolutePath());
            } catch (Exception ex) {
                this.output.rewrite("[<red> x </red>] Installing composer dependencies: <red>" + ex.getMessage() + "</red>");
                return false;
            }
            this.output.rewrite("[<green> ✔️ </green>] Installing composer dependencies");
        }

        return true;
    }
}
