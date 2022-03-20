package com.pipan.elephant.service;

import com.pipan.elephant.shell.Shell;

public class NginxService {
    private Shell shell;

    public NginxService(Shell shell) {
        this.shell = shell;
    }

    public void restart() throws Exception {
        this.shell.runWithException("sudo", "systemctl", "restart", "nginx");
    }

    public void reload() throws Exception {
        this.shell.runWithException("sudo", "systemctl", "reload", "nginx");
    }
}
