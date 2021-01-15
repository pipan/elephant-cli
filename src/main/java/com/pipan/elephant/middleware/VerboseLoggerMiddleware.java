package com.pipan.elephant.middleware;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.middleware.BaseMiddleware;
import com.pipan.elephant.log.SystemLogger;

public class VerboseLoggerMiddleware extends BaseMiddleware {
    protected SystemLogger logger;

    public VerboseLoggerMiddleware(SystemLogger logger) {
        super();
        this.logger = logger;
    }

    @Override
    public CommandResult beforeAction(Command command) throws Exception {
        if (command.hasSwitch("verbose")) {
            this.logger.enableLevelAll();
        }

        return super.beforeAction(command);
    }
}
