package com.pipan.elephant.hook;

import java.util.List;

import com.pipan.elephant.shell.Shell;
import com.pipan.filesystem.File;

public class HookChain implements Hook {
    private List<Hook> hooks;

    public HookChain(List<Hook> hooks) {
        this.hooks = hooks;
    }

    public void execute() throws Exception {
        for (Hook hook : this.hooks) {
            hook.execute();
        }
    }
}