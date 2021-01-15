package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.ControllerWithMiddlewares;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.middleware.ValidateElephantFileMiddleware;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.elephant.workingdir.WorkingDirectoryFactory;
import com.pipan.elephant.workingdir.WorkingDirectoryFormater;

public class StatusController extends ControllerWithMiddlewares {
    private WorkingDirectoryFactory workingDirectoryFactory;
    private Shell shell;

    public StatusController(WorkingDirectoryFactory workingDirectoryFactory, Shell shell) {
        this.workingDirectoryFactory = workingDirectoryFactory;
        this.shell = shell;

        this.withMiddleware(new ValidateElephantFileMiddleware(this.workingDirectoryFactory, this.shell));
    }

    @Override
    public CommandResult action(Command command) throws Exception {
        WorkingDirectory workingDirectory = this.workingDirectoryFactory.create(command);

        if (!workingDirectory.getReleasesDirectory().exists()) {
            this.shell.out("Available releases: no release");
            return CommandResult.ok("No release");
        }

        if (workingDirectory.getReleasesDirectory().getDirectoryList().isEmpty()) {
            this.shell.out("Available releases: no release");
            return CommandResult.ok("No release");
        }

        WorkingDirectoryFormater formater = new WorkingDirectoryFormater();
        String status = formater.format(workingDirectory);

        this.shell.out("Available releases:\n" + status);
        return CommandResult.ok();
    }
}
