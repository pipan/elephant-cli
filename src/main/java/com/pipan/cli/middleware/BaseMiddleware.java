package com.pipan.cli.middleware;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;

public class BaseMiddleware implements Middleware {
    @Override
    public CommandResult afterAction(CommandResult result) throws Exception {
        return result;
    }

    @Override
    public CommandResult beforeAction(Command command) throws Exception {
        return null;
    }
}
