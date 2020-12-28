package com.pipan.cli.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;

abstract public class HookController implements Controller {
    @Override
    public CommandResult execute(Command command) throws Exception {
        CommandResult result = this.beforeAction(command);
        if (result == null) {
            result = this.action(command);
        }
        return this.afterAction(result);
    }

    protected CommandResult beforeAction(Command command) throws Exception {
        return null;
    }

    protected CommandResult afterAction(CommandResult result) throws Exception {
        return result;
    }

    abstract protected CommandResult action(Command command) throws Exception;
}
