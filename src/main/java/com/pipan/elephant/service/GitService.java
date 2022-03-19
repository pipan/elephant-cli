package com.pipan.elephant.service;

import com.pipan.elephant.shell.Shell;

public class GitService {
    private Shell shell;

    public GitService(Shell shell) {
        this.shell = shell;
    }

    public void clone(String url, String directory) throws Exception {
        this.shell.runWithException("git", "clone", "-q", url, directory);
    }   
}
