package com.pipan.elephant.hook;

import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.shell.Shell;
import com.pipan.filesystem.File;

public class FileHook implements Hook {
    private File file;
    private Shell shell;
    private ConsoleOutput output;
    private String base;

    public FileHook(File file, String base, Shell shell, ConsoleOutput output) {
        this.file = file;
        this.shell = shell;
        this.output = output;
        this.base = base;
    }

    public void execute() throws Exception {
        if (this.file == null || !this.file.exists()) {
            return;
        }
        this.output.write("[...] Executing file hook " + this.file.getName());
        this.shell.runWithException(this.base + java.io.File.separator + this.file.getName(), this.base);
        this.output.rewrite("[  ] Executing file hook " + this.file.getName());
    }
}