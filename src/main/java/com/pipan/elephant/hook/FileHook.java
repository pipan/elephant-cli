package com.pipan.elephant.hook;

import com.pipan.elephant.log.Logger;
import com.pipan.elephant.shell.Shell;
import com.pipan.filesystem.File;

public class FileHook implements Hook {
    private File file;
    private Shell shell;
    private Logger logger;
    private String base;

    public FileHook(File file, String base, Shell shell, Logger logger) {
        this.file = file;
        this.shell = shell;
        this.logger = logger;
        this.base = base;
    }

    public void execute() {
        if (this.file == null || !this.file.exists()) {
            return;
        }
        this.logger.info("Executing file hook " + this.file.getName());
        this.shell.run("./" + this.file.getName() + " " + this.base);
        this.logger.info("Executing file hook " + this.file.getName() + ": done");
    }
}