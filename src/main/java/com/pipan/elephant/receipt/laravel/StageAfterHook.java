package com.pipan.elephant.receipt.laravel;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

import com.pipan.elephant.hook.Hook;
import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Directory;
import com.pipan.filesystem.File;
import com.pipan.filesystem.SymbolicLink;
import com.pipan.filesystem.DiskSymbolicLink;

public class StageAfterHook implements Hook {
    private WorkingDirectory workingDirectory;
    private ConsoleOutput output;

    public StageAfterHook(WorkingDirectory workingDirectory) {
        this.workingDirectory = workingDirectory;
        this.output = new ConsoleOutput();
    }

    public void execute() throws Exception {
        File envFile = workingDirectory.getFilesystem().getFile(".env");
        this.output.write("[...] Copying .env file");
        if (!envFile.exists()) {
            this.output.rewrite("[<yellow> - </yellow>] Copying .env file: <yellow>file does not exists</yellow>");
        } else {
            Files.copy(Paths.get(envFile.getAbsolutePath()), Paths.get(workingDirectory.getStageLink().getTargetString(), envFile.getName()), StandardCopyOption.REPLACE_EXISTING);
            this.output.rewrite("[<green> ✔️ </green>] Copying .env file");
        }

        Directory stageStorageDir = workingDirectory.getStageLink().getTargetDirectory().enterDir("storage");
        SymbolicLink storageLink = new DiskSymbolicLink(stageStorageDir.getAbsolutePath());
        Directory storageDir = workingDirectory.getFilesystem().getDirectory("storage");
        if (!storageDir.exists()) {
            this.output.write("[...] Creating storage");
            try {
                this.copyDirectory(Paths.get(stageStorageDir.getAbsolutePath()), Paths.get(storageDir.getAbsolutePath()));
            } catch (IOException ex) {
                this.output.rewrite("[<red> x </red>] Creating storage: <red>" + ex.getMessage() + "</red>");
                throw new LaravelReceiptException("Cannot create storage directory", ex);
            }
            this.output.rewrite("[<green> ✔️ </green>] Creating storage");
        }

        this.output.write("[...] Linking storage");
        stageStorageDir.delete();
        storageLink.setTarget(storageDir.getAbsolutePath());
        this.output.rewrite("[<green> ✔️ </green>] Linking storage");
    }

    private void copyDirectory(Path sourceDir, Path destinationDir) throws IOException {
        Files.walk(sourceDir).forEach(sourcePath -> {
            try {
                Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                Files.copy(sourcePath, targetPath);
            } catch (IOException ex) {
                throw new RuntimeException("copy directory", ex);
            }
        });
    }
}