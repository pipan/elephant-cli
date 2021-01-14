package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.elephant.Resource;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.elephant.workingdir.WorkingDirectoryFactory;
import com.pipan.filesystem.File;

public class InitController implements Controller {
    private WorkingDirectoryFactory workingDirectoryFactory;
    private Shell shell;

    public InitController(WorkingDirectoryFactory workingDirectoryFactory, Shell shell) {
        this.workingDirectoryFactory = workingDirectoryFactory;
        this.shell = shell;
    }

    public CommandResult execute(Command command) throws Exception {
        WorkingDirectory workingDirectory = this.workingDirectoryFactory.create(command);
        File config = workingDirectory.getConfigFile();

        if (config.exists()) {
            this.shell.err("Elephant file exists");
            return CommandResult.fail("Elephant file exists");
        }

        config.write(Resource.getContent("template/elephant.json"));

        this.shell.out("Elephant file was created with default values. You should edit this file.");
        return CommandResult.ok();
    }
}
