package com.pipan.elephant.service;

import com.pipan.elephant.shell.Shell;

public class ApacheService {
    private Shell shell;

    public ApacheService(Shell shell) {
        this.shell = shell;
    }

    public void restartFpm(String version) throws Exception {
        this.shell.runWithException("sudo", "systemctl", "restart", "php-fpm" + version);
    }

    public void restartFpm() throws Exception {
        this.restartFpm("");
    }

    public void reloadFpm(String version) throws Exception {
        this.shell.runWithException("sudo", "systemctl", "reload", "php-fpm" + version);
    }

    public void reloadFpm() throws Exception {
        this.reloadFpm("");
    }
}
