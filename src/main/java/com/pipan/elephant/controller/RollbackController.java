package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.ControllerWithMiddlewares;
import com.pipan.elephant.cleaner.Cleaner;
import com.pipan.elephant.cleaner.UnusedStageCleaner;
import com.pipan.elephant.middleware.InitRequiredMiddleware;
import com.pipan.elephant.progress.Progress;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.service.ApacheService;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Directory;

public class RollbackController extends ControllerWithMiddlewares {
    private Releases releases;
    private WorkingDirectory workingDirectory;
    private ApacheService apache;
    private Progress progress;

    public RollbackController(Releases releases, ApacheService apache, Progress progress) {
        super();
        this.releases = releases;
        this.workingDirectory = this.releases.getWorkingDirectory();
        this.apache = apache;
        this.progress = progress;

        this.withMiddlewae(new InitRequiredMiddleware(this.workingDirectory));
    }

    @Override
    protected CommandResult action(Command command) throws Exception {
        Directory previous = this.releases.getPreviousDirectory();
        if (previous == null) {
            return CommandResult.fail("No rollback version available");
        }

        this.workingDirectory.getProductionLink().setTarget(
            previous.getAbsolutePath()
        );        

        this.progress.info("Reloading php fpm");
        this.apache.reloadFpm();

        Cleaner stageCleaner = new UnusedStageCleaner(this.releases.getWorkingDirectory());
        stageCleaner.clean();

        return CommandResult.ok("Done");
    }
}
