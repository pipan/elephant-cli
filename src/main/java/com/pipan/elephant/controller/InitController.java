package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.elephant.Resource;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.elephant.workingdir.WorkingDirectoryFactory;
import com.pipan.filesystem.File;

public class InitController implements Controller {
    private WorkingDirectoryFactory workingDirectoryFactory;
    private Shell shell;
    private Logger logger;

    public InitController(WorkingDirectoryFactory workingDirectoryFactory, Shell shell, Logger logger) {
        this.workingDirectoryFactory = workingDirectoryFactory;
        this.shell = shell;
        this.logger = logger;
    }

    public CommandResult execute(Command command) throws Exception {
        WorkingDirectory workingDirectory = this.workingDirectoryFactory.create(command);
        File config = workingDirectory.getConfigFile();

        if (!config.exists()) {
            this.logger.info("Creating elephant file");
            config.write(Resource.getContent("template/elephant.json"));
            this.logger.info("Creating elephant file: done");
        }

        if (!workingDirectory.getReleasesDirectory().exists()) {
            this.logger.info("Creating releases directory");
            workingDirectory.getReleasesDirectory().make();
            this.logger.info("Creating releases directory: done");
        }

        this.shell.out("Initialization successful");
        return CommandResult.ok();
    }
}
