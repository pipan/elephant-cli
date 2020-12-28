package com.pipan.elephant.middleware;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.middleware.BaseMiddleware;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.File;

public class InitRequiredMiddleware extends BaseMiddleware {
    protected WorkingDirectory workingDir;

    public InitRequiredMiddleware(WorkingDirectory workingDir) {
        super();
        this.workingDir = workingDir;
    }

    @Override
    public CommandResult beforeAction(Command command) throws Exception {
        File config = this.workingDir.getConfigFile();

        if (!config.exists()) {
            return CommandResult.fail("Config file not found");
        }

        return super.beforeAction(command);
    }
}
