package com.pipan.elephant.action;

import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.output.Emoji;
import com.pipan.elephant.service.ApacheService;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.config.ElephantConfig;

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