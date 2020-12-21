package com.pipan.elephant.service;

import com.pipan.elephant.shell.Shell;

public class GitService {
    private Shell shell;

    public GitService(Shell shell) {
        this.shell = shell;
    }

    public boolean clone(String url, String directory) throws Exception {
        return this.shell.runWithException("git clone " + url + " " + directory);
    }   
}
