package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;

public class HelpController implements Controller {
    public CommandResult execute(Command command) throws Exception {
        System.out.println("Available Commands:");
        System.out.println("init       - ");
        System.out.println("status     - ");
        System.out.println("stage      - ");
        System.out.println("upgrade    - ");
        System.out.println("rollback   - ");
        System.out.println("config:get - TODO");
        System.out.println("config:set - TODO");
        System.out.println("fix        - TODO");
        return CommandResult.ok("");
    }
}