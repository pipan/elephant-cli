package com.pipan.elephant.service;

import com.pipan.elephant.shell.Shell;

public class ComposerService {
    private Shell shell;

    public ComposerService(Shell shell) {
        this.shell = shell;
    }

    public boolean install(String directory) throws Exception {
        return this.shell.runWithException("composer install --no-dev -o -d " + directory);
    }

    public boolean createProject(String packageName, String directory) throws Exception {
        return this.shell.runWithException("composer create-project --no-cache --no-dev --prefer-dist " + packageName + " " + directory);
    }
}
