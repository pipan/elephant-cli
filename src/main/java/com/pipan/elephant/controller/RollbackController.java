package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.ControllerWithMiddlewares;
import com.pipan.elephant.action.ActionHooks;
import com.pipan.elephant.action.FpmAction;
import com.pipan.elephant.cleaner.UnusedStageCleaner;
import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.config.ElephantConfigFactory;
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
    protected FpmAction fpmAction;
    protected Logger logger;
    protected ActionHooks actionHooks;

    public RollbackController(WorkingDirectoryFactory workingDirectoryFactory, Shell shell, Logger logger) {
        super();
        this.workingDirectoryFactory = workingDirectoryFactory;
        this.shell = shell;
        this.logger = logger;
        this.fpmAction = new FpmAction(new ApacheService(shell), this.logger);
        this.actionHooks = new ActionHooks("rollback", this.shell, this.logger);
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
        
        ElephantConfig config = (new ElephantConfigFactory()).create(workingDirectory.getConfigFile());

        this.actionHooks.dispatchBefore(workingDirectory);

        this.logger.info("Set production link");
        workingDirectory.getProductionLink().setTarget(previous.getAbsolutePath());
        this.logger.info("Set production link: done");

        this.fpmAction.run(config);

        (new UnusedStageCleaner(workingDirectory)).clean();

        this.actionHooks.dispatchAfter(workingDirectory);

        this.shell.out("Rollback successful");
        return CommandResult.ok();
    }
}
