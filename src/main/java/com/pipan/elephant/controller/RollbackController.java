package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.ControllerWithMiddlewares;
import com.pipan.elephant.action.ActionHooks;
import com.pipan.elephant.action.FpmAction;
import com.pipan.elephant.cleaner.UnusedStageCleaner;
import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.config.ElephantConfigFactory;
import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.output.Emoji;
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
    protected ActionHooks actionHooks;
    private ConsoleOutput output;

    public RollbackController(WorkingDirectoryFactory workingDirectoryFactory, Shell shell, ConsoleOutput output) {
        super();
        this.workingDirectoryFactory = workingDirectoryFactory;
        this.shell = shell;
        this.output = output;
        this.fpmAction = new FpmAction(new ApacheService(shell), output);
        this.actionHooks = new ActionHooks("rollback", this.shell, output);
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

        this.output.write("[...] Activate previous version");
        Boolean stageEqualsProducion = releases.stageEqualsProduction();
        
        workingDirectory.getProductionLink().setTarget(previous.getAbsolutePath());
        if (stageEqualsProducion) {
            workingDirectory.getStageLink().getTargetDirectory().delete();
            workingDirectory.getStageLink().setTarget(previous.getAbsolutePath());
        }
        
        this.output.rewrite("[<green> " + Emoji.CHECK_MARK + " </green>] Activate previous version");

        this.fpmAction.run(config);

        (new UnusedStageCleaner(workingDirectory)).clean();

        this.actionHooks.dispatchAfter(workingDirectory);

        this.output.write("<green>Rollback successful</green>");
        return CommandResult.ok();
    }
}
