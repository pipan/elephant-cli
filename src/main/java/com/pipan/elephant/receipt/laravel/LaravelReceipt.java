package com.pipan.elephant.receipt.laravel;

import com.pipan.elephant.receipt.Receipt;
import com.pipan.elephant.hook.Hook;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.workingdir.WorkingDirectory;

public class LaravelReceipt extends Receipt {
    private Logger logger;

    public LaravelReceipt(Logger logger) {
        this.logger = logger;
    }

    public Hook getStageAfterHook(WorkingDirectory workingDirectory) {
        return new StageAfterHook(workingDirectory, this.logger);
    }
}