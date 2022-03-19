package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.elephant.Resource;
import com.pipan.elephant.shell.Shell;

public class HelpController implements Controller {
    private Shell shell;

    public HelpController(Shell shell) {
        this.shell = shell;
    }

    public CommandResult execute(Command command) throws Exception {
        this.shell.out(Resource.getContent("help.txt"));
        return CommandResult.ok();
    }
}