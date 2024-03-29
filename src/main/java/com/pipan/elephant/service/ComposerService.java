package com.pipan.elephant.service;

import com.pipan.elephant.shell.Shell;

public class ComposerService {
    private Shell shell;

    public ComposerService(Shell shell) {
        this.shell = shell;
    }

    public void install(String directory) throws Exception {
        this.shell.runWithException("composer", "-q", "install", "--no-dev", "-o", "-d", directory);
    }

    public void createProject(String packageName, String directory) throws Exception {
        this.shell.runWithException("composer", "-q", "create-project", "--no-cache", "--no-dev", "--prefer-dist", "--no-progress", packageName, directory);
    }
}
