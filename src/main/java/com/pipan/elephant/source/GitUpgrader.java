package com.pipan.elephant.source;

import com.pipan.elephant.config.Config;
import com.pipan.elephant.progress.Progress;
import com.pipan.elephant.service.ComposerService;
import com.pipan.elephant.service.GitService;
import com.pipan.filesystem.Directory;

public class GitUpgrader implements Upgrader {
    private GitService git;
    private ComposerService composer;
    private Progress progress;

    public GitUpgrader(GitService git, ComposerService composer, Progress progress) {
        this.git = git;
        this.composer = composer;
        this.progress = progress;
    }

    public boolean upgrade(Directory dir, Config config) throws Exception {            
        this.progress.info("Executing source code checkout from git");
        Boolean result = this.git.clone(config.getString("url"), dir.getAbsolutePath());
        if (!result) {
            return result;
        }

        if (config.getBoolean("composer", false)) {
            this.progress.info("Installing composer dependencies");
            result = this.composer.install(dir.getAbsolutePath());
        }

        return result;
    }
}
