package com.pipan.elephant.action;

import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.output.Emoji;
import com.pipan.elephant.service.NginxService;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.config.ElephantConfig;

public class NginxAction {
    protected NginxService nginxService;
    protected ConsoleOutput output;

    public NginxAction(NginxService nginxService, ConsoleOutput output) {
        this.nginxService = nginxService;
        this.output = output;
    }

    public void run(ElephantConfig config) throws Exception {
        if (!config.getNginx()) {
            return;
        }

        this.output.write("[...] Reloading nginx");
        try {
            this.nginxService.reload();
        } catch (InterruptedException ex) {
            this.output.rewrite("[<red> " + Emoji.CROSS + " </red>] Reloading nginx: <red>" + ex.getMessage() + "</red>");
            return;
        }
        
        this.output.rewrite("[<green> " + Emoji.CHECK_MARK + " </green>] Reloading nginx");
    }
}