package com.pipan.cli.middleware;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;

public interface Middleware {
    public CommandResult beforeAction(Command command) throws Exception;
    public CommandResult afterAction(CommandResult result) throws Exception;
}
