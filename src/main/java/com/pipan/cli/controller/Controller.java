package com.pipan.cli.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;

public interface Controller {
    public CommandResult execute(Command command) throws Exception;
}
