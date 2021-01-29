package com.pipan.elephant.receipt.laravel;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

import com.pipan.elephant.hook.Hook;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Directory;
import com.pipan.filesystem.File;
import com.pipan.filesystem.SymbolicLink;
import com.pipan.filesystem.DiskSymbolicLink;

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

        Directory stageStorageDir = workingDirectory.getStageLink().getTargetDirectory().enterDir("storage");
        SymbolicLink storageLink = new DiskSymbolicLink(stageStorageDir.getAbsolutePath());
        Directory storageDir = workingDirectory.getFilesystem().getDirectory("storage");
        if (!storageDir.exists()) {
            this.logger.info("Creating storage");
            this.copyDirectory(
                Paths.get(stageStorageDir.getAbsolutePath()),
                Paths.get(storageDir.getAbsolutePath())
            );
            this.logger.info("Creating storage: done");
        }

        this.logger.info("Linking storage");
        stageStorageDir.delete();
        storageLink.setTarget(storageDir.getAbsolutePath());
        this.logger.info("Linking storage: done");
    }

    private void copyDirectory(Path sourceDir, Path destinationDir) {
        try {
            Files.walk(sourceDir)
                .forEach(sourcePath -> {
                    try {
                        Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                        Files.copy(sourcePath, targetPath);
                    } catch (IOException ex) {
                        this.logger.warning("Cannot copy directory");
                    }
                });
        } catch (IOException ex) {
            this.logger.warning("Cannot copy directory");
        }
    }
}