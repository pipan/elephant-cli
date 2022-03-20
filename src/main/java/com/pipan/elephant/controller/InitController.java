package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.elephant.Resource;
import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.output.Emoji;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.elephant.workingdir.WorkingDirectoryFactory;
import com.pipan.filesystem.File;

public class InitController implements Controller {
    private WorkingDirectoryFactory workingDirectoryFactory;
    private ConsoleOutput output;

    public InitController(WorkingDirectoryFactory workingDirectoryFactory, ConsoleOutput output) {
        this.workingDirectoryFactory = workingDirectoryFactory;
        this.output = output;
    }

    public CommandResult execute(Command command) throws Exception {
        WorkingDirectory workingDirectory = this.workingDirectoryFactory.create(command);
        File config = workingDirectory.getConfigFile();

        this.output.write("[...] Creating elephant file");
        if (!config.exists()) {
            config.write(Resource.getContent("template/elephant.json"));
            this.output.rewrite("[<green> " + Emoji.CHECK_MARK + " </green>] Creating elephant file");
        } else {
            this.output.rewrite("[<yellow> ⚠ </yellow>] Creating elephant file: <yellow>file already exists.</yellow>");
        }

        this.output.write("[...] Creating releases directory");
        if (!workingDirectory.getReleasesDirectory().exists()) {
            workingDirectory.getReleasesDirectory().make();
            this.output.rewrite("[<green> " + Emoji.CHECK_MARK + " </green>] Creating releases directory");
        } else {
            this.output.rewrite("[<yellow> " + Emoji.WARNING + " </yellow>] Creating releases directory: <yellow>release directory already exists.</yellow>");
        }

        this.output.write("<green>Initialization finnished</green>");
        
        return CommandResult.ok();
    }
}
