package com.pipan.elephant.action;

import java.util.Arrays;

import com.pipan.elephant.cleaner.UnusedStageCleaner;
import com.pipan.elephant.generator.IncrementalDirectoryGenerator;
import com.pipan.elephant.hook.Hook;
import com.pipan.elephant.hook.HookChain;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.receipt.Receipt;
import com.pipan.elephant.repository.Repository;
import com.pipan.elephant.service.ApacheService;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.hook.FileHook;
import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.config.ElephantConfigFactory;
import com.pipan.elephant.source.Upgrader;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Directory;
import com.pipan.filesystem.Filesystem;

public class FpmAction {
    protected ApacheService apacheService;
    protected Logger logger;

    public FpmAction(ApacheService apacheService, Logger logger) {
        this.apacheService = apacheService;
        this.logger = logger;
    }

    public void run(ElephantConfig config) throws Exception {
        FpmConfig fpmConfig = new FpmConfig(config.getFpm());
        if (!fpmConfig.isEnabled()) {
            return;
        }

        if (fpmConfig.getCommand().equals("reload")) {
            this.logger.info("Reloading php fpm");
            this.apacheService.reloadFpm(fpmConfig.getVersion());
            this.logger.info("Reloading php fpm: done");
            return;
        }
    }
}