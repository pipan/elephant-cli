package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.ControllerWithMiddlewares;
import com.pipan.elephant.app.configuration.ProxyConfiguration;
import com.pipan.elephant.cleaner.UnusedStageCleaner;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.service.ApacheService;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.elephant.workingdir.WorkingDirectoryFactory;
import com.pipan.filesystem.Directory;

public class RollbackController extends ControllerWithMiddlewares {
    protected WorkingDirectoryFactory workingDirectoryFactory;
    protected Shell shell;
    protected ApacheService apache;
    protected Logger logger;

    public RollbackController(WorkingDirectoryFactory workingDirectoryFactory, Shell shell, Logger logger) {
        super();
        this.workingDirectoryFactory = workingDirectoryFactory;
        this.shell = shell;
        this.logger = logger;
        this.apache = new ApacheService(shell);
    }

    @Override
    protected CommandResult action(Command command) throws Exception {
        WorkingDirectory workingDirectory = this.workingDirectoryFactory.create(command);
        if (!workingDirectory.getReleasesDirectory().exists()) {
            throw new Exception("No rollback version available");
        }

        Releases releases = new Releases(workingDirectory);
        Directory previous = releases.getPreviousDirectory();
        if (previous == null) {
            throw new Exception("No rollback version available");
        }

        this.logger.info("Set production link");
        workingDirectory.getProductionLink().setTarget(previous.getAbsolutePath());
        this.logger.info("Set production link: done");

        this.logger.info("Reloading php fpm");
        this.apache.reloadFpm();
        this.logger.info("Reloading php fpm: done");

        (new UnusedStageCleaner(workingDirectory)).clean();

        this.shell.out("Rollback successful");
        return CommandResult.ok();
    }
}
