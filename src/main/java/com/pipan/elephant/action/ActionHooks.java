package com.pipan.elephant.action;

import java.util.Arrays;

import com.pipan.elephant.hook.Hook;
import com.pipan.elephant.hook.HookChain;
import com.pipan.elephant.hook.FileHook;
import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.receipt.Receipt;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Filesystem;

public class ActionHooks {
    private String actionName;
    private Shell shell;
    private ConsoleOutput output;
    private Receipt receipt;

    public ActionHooks(String actionName, Shell shell, ConsoleOutput output) {
        this(actionName, shell, output, new Receipt());
    }

    public ActionHooks(String actionName, Shell shell, ConsoleOutput output, Receipt receipt) {
        this.actionName = actionName;
        this.shell = shell;
        this.output = output;
        this.receipt = receipt;
        if (this.receipt == null) {
            this.receipt = new Receipt();
        }
    }

    public void dispatchBefore(WorkingDirectory workingDirectory) throws Exception {
        Filesystem filesystem = workingDirectory.getFilesystem();
        Hook hooks = new HookChain(Arrays.asList(
            new FileHook(filesystem.getFile(this.actionName + ".before"), filesystem.getBase(), this.shell, this.output),
            this.receipt.getStageBeforeHook(workingDirectory)
        ));

        hooks.execute();
    }

    public void dispatchAfter(WorkingDirectory workingDirectory) throws Exception {
        Filesystem filesystem = workingDirectory.getFilesystem();
        Hook hooks = new HookChain(Arrays.asList(
            new FileHook(filesystem.getFile(this.actionName + ".after"), filesystem.getBase(), this.shell, this.output),
            this.receipt.getStageAfterHook(workingDirectory)
        ));

        hooks.execute();
    }
}