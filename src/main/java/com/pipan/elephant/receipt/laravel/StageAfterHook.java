package com.pipan.elephant.receipt.laravel;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.pipan.elephant.hook.Hook;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.File;

public class StageAfterHook implements Hook {
    private WorkingDirectory workingDirectory;
    private Logger logger;

    public StageAfterHook(WorkingDirectory workingDirectory, Logger logger) {
        this.workingDirectory = workingDirectory;
        this.logger = logger;
    }

    public void execute() throws Exception {
        File envFile = workingDirectory.getFilesystem().getFile(".env");
        if (!envFile.exists()) {
            this.logger.warning("Cannot copy .env file: File does not exists");
        } else {
            this.logger.info("Copying .env file");
            Files.copy(Paths.get(envFile.getAbsolutePath()), Paths.get(workingDirectory.getStageLink().getTargetString(), envFile.getName()), StandardCopyOption.REPLACE_EXISTING);
            this.logger.info("Copying .env file: done");
        }

        // this.logger.info("Setting storage permissions");
        // java.io.File storageDir = new java.io.File(workingDirectory.getStageLink().getTargetString());
        // storageDir.setExecutable(true, false);
        // storageDir.setWritable(true, false);
        // storageDir.setReadable(true, false);
        // this.logger.info("Setting storage permissions: done");
    }
}