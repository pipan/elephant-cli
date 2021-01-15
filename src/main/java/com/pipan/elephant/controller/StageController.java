package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.ControllerWithMiddlewares;
import com.pipan.elephant.cleaner.Cleaner;
import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.config.ElephantConfigFactory;
import com.pipan.elephant.middleware.ValidateElephantFileMiddleware;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.source.Upgrader;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.elephant.workingdir.WorkingDirectoryFactory;
import com.pipan.elephant.action.StageAction;
import com.pipan.filesystem.Directory;

public class StageController extends ControllerWithMiddlewares {
    private WorkingDirectoryFactory workingDirectoryFactory;
    private Shell shell;
    private UpgraderRepository upgraderRepository;

    public StageController(WorkingDirectoryFactory workingDirectoryFactory, Shell shell, UpgraderRepository upgraderRepository) {
        super();
        this.workingDirectoryFactory = workingDirectoryFactory;
        this.shell = shell;
        this.upgraderRepository = upgraderRepository;

        this.withMiddleware(new ValidateElephantFileMiddleware(this.workingDirectoryFactory, this.shell));
    }

    @Override
    protected CommandResult action(Command command) throws Exception {
        WorkingDirectory workingDirectory = this.workingDirectoryFactory.create(command);

        (new StageAction(this.upgraderRepository)).stage(workingDirectory);
        
        this.shell.out("Stage successful");
        return CommandResult.ok();
    }
}
