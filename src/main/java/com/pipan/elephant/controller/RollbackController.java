package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.elephant.cleaner.Cleaner;
import com.pipan.elephant.cleaner.UnusedStageCleaner;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.service.ApacheService;
import com.pipan.filesystem.Directory;

public class RollbackController extends RequireInitController {
    private Releases releases;
    private ApacheService apache;

    public RollbackController(Releases releases, ApacheService apache) {
        super(releases.getWorkingDirectory());
        this.releases = releases;
        this.apache = apache;
    }

    @Override
    protected CommandResult doExecute(Command command) throws Exception {
        Directory previous = this.releases.getPreviousDirectory();
        if (previous == null) {
            return CommandResult.fail("No rollback version available");
        }

        this.workingDir.getProductionLink().setTarget(
            previous.getAbsolutePath()
        );        

        System.out.println("[ INFO ] Restarting php fpm");
        this.apache.reloadFpm();

        Cleaner stageCleaner = new UnusedStageCleaner(this.releases.getWorkingDirectory());
        stageCleaner.clean();

        return CommandResult.ok("Done");
    }
}
