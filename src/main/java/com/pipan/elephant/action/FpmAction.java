package com.pipan.elephant.action;

import java.util.Arrays;

import com.pipan.elephant.cleaner.UnusedStageCleaner;
import com.pipan.elephant.generator.IncrementalDirectoryGenerator;
import com.pipan.elephant.hook.Hook;
import com.pipan.elephant.hook.HookChain;
import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.output.Emoji;
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
    protected ConsoleOutput output;

    public FpmAction(ApacheService apacheService, ConsoleOutput output) {
        this.apacheService = apacheService;
        this.output = output;
    }

    public void run(ElephantConfig config) throws Exception {
        FpmConfig fpmConfig = new FpmConfig(config.getFpm());
        if (!fpmConfig.isEnabled()) {
            return;
        }

        if (fpmConfig.getCommand().equals("reload")) {
            this.output.write("[...] Reloading php fpm");
            try {
                this.apacheService.reloadFpm(fpmConfig.getVersion());
            } catch (InterruptedException ex) {
                this.output.rewrite("[<red> " + Emoji.CROSS + " </red>] Reloading php fpm: <red>" + ex.getMessage() + "</red>");
                return;
            }
            
            this.output.rewrite("[<green> " + Emoji.CHECK_MARK + " </green>] Reloading php fpm");
            return;
        }
    }
}