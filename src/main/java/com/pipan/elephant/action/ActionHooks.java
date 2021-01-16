package com.pipan.elephant.action;

import java.util.Arrays;

import com.pipan.elephant.hook.Hook;
import com.pipan.elephant.hook.HookChain;
import com.pipan.elephant.hook.FileHook;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Filesystem;

public class ActionHooks {
    private String actionName;
    private Shell shell;
    private Logger logger;

    public ActionHooks(String actionName, Shell shell, Logger logger) {
        this.actionName = actionName;
        this.shell = shell;
        this.logger = logger;
    }

    public void dispatchBefore(WorkingDirectory workingDirectory) throws Exception {
        Filesystem filesystem = workingDirectory.getFilesystem();
        Hook hooks = new HookChain(Arrays.asList(
            new FileHook(filesystem.getFile(this.actionName + ".before"), filesystem.getBase(), this.shell, this.logger)
        ));

        hooks.execute();
    }

    public void dispatchAfter(WorkingDirectory workingDirectory) throws Exception {
        Filesystem filesystem = workingDirectory.getFilesystem();
        Hook hooks = new HookChain(Arrays.asList(
            new FileHook(filesystem.getFile(this.actionName + ".after"), filesystem.getBase(), this.shell, this.logger)
        ));

        hooks.execute();
    }
}