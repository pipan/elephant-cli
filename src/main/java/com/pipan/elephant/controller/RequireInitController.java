package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.File;

abstract public class RequireInitController implements Controller {
    protected WorkingDirectory workingDir;

    public RequireInitController(WorkingDirectory workingDir) {
        this.workingDir = workingDir;
    }

    abstract protected CommandResult doExecute(Command command) throws Exception;

    public CommandResult execute(Command command) throws Exception {
        File config = this.workingDir.getConfigFile();

        if (!config.exists()) {
            return CommandResult.fail("Config file not found");
        }

        return this.doExecute(command);
    }
}
