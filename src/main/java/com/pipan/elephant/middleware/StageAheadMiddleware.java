package com.pipan.elephant.middleware;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.middleware.Middleware;
import com.pipan.elephant.release.Releases;

public class StageAheadMiddleware implements Middleware {
    private Releases releases;

    public StageAheadMiddleware(Releases releases) {
        this.releases = releases;
    }

    @Override
    public CommandResult beforeAction(Command command) {
        if (this.releases.isProductionAhead()) {
            return CommandResult.fail("Stage link is behind production. Fix stage link to continue.");
        }
        return null;
        
    }

    @Override
    public void afterAction(CommandResult result) {}
}
