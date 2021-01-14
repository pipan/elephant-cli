package com.pipan.elephant.source.git;

import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.service.ComposerService;
import com.pipan.elephant.service.GitService;
import com.pipan.elephant.source.Upgrader;
import com.pipan.filesystem.Directory;

public class GitUpgrader implements Upgrader {
    private GitService git;
    private ComposerService composer;
    private Logger logger;

    public GitUpgrader(GitService git, ComposerService composer, Logger logger) {
        this.git = git;
        this.composer = composer;
        this.logger = logger;
    }

    public boolean upgrade(Directory dir, ElephantConfig config) throws Exception {    
        GitSourceConfig gitConfig = new GitSourceConfig(config.getSource());
        this.logger.info("Executing source code checkout from git");
        Boolean result = this.git.clone(gitConfig.getUrl(), dir.getAbsolutePath());
        if (!result) {
            return result;
        }

        if (gitConfig.hasComposer()) {
            this.logger.info("Installing composer dependencies");
            result = this.composer.install(dir.getAbsolutePath());
        }

        return result;
    }
}
