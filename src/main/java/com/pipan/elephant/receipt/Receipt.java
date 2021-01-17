package com.pipan.elephant.receipt;

import com.pipan.elephant.hook.Hook;
import com.pipan.elephant.workingdir.WorkingDirectory;

public class Receipt {
    public Hook getStageAfterHook(WorkingDirectory workingDirectory) {
        return new EmptyHook();
    }

    public Hook getStageBeforeHook(WorkingDirectory workingDirectory) {
        return new EmptyHook();
    }
}