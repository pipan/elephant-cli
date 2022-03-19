package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.elephant.Resource;
import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.shell.Shell;

public class HelpController implements Controller {
    private Shell shell;
    private ConsoleOutput output;

    public HelpController(Shell shell, ConsoleOutput output) {
        this.shell = shell;
        this.output = output;
    }

    public CommandResult execute(Command command) throws Exception {
        this.shell.out(Resource.getContent("help.txt"));
        return CommandResult.ok();
    }
}