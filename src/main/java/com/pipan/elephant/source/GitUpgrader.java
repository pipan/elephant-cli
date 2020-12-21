package com.pipan.elephant.source;

import com.pipan.elephant.config.Config;
import com.pipan.elephant.service.ComposerService;
import com.pipan.elephant.service.GitService;
import com.pipan.filesystem.Directory;

public class GitUpgrader implements Upgrader {
    private GitService git;
    private ComposerService composer;

    public GitUpgrader(GitService git, ComposerService composer) {
        this.git = git;
        this.composer = composer;
    }

    public boolean upgrade(Directory dir, Config config) throws Exception {            
        System.out.println("[ INFO ] Executing source code checkout from git");
        Boolean result = this.git.clone(config.getString("url"), dir.getAbsolutePath());
        if (!result) {
            return result;
        }

        if (config.getBoolean("composer", false)) {
            System.out.println("[ INFO ] Installing composer dependencies");
            result = this.composer.install(dir.getAbsolutePath());
        }

        return result;
    }
}
