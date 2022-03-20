package com.pipan.elephant.service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.pipan.elephant.shell.Shell;

public class GitService {
    private Shell shell;

    public GitService(Shell shell) {
        this.shell = shell;
    }

    public void clone(String url, String directory) throws Exception {
        this.clone(url, directory, "");
    }
    
    public void clone(String url, String directory, String branch) throws Exception {
        List<String> commandList = new LinkedList();
        commandList.addAll(Arrays.asList("git", "clone", "-q"));
        if (!branch.isEmpty()) {
            commandList.addAll(Arrays.asList("--single-branch", "-b", branch));
        }
        commandList.addAll(Arrays.asList(url, directory));
        this.shell.runWithException(commandList);
    }
}
