package com.pipan.elephant.middleware;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.middleware.BaseMiddleware;
import com.pipan.elephant.release.Releases;

public class StageAheadMiddleware extends BaseMiddleware {
    private Releases releases;

    public StageAheadMiddleware(Releases releases) {
        super();
        this.releases = releases;
    }

    @Override
    public CommandResult beforeAction(Command command) throws Exception {
        if (this.releases.isProductionAhead()) {
            return CommandResult.fail("Cannot create new stage: stage is behind production");
        }
        return super.beforeAction(command);
    }
}
