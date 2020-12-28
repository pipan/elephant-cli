package com.pipan.cli.controller;

import java.util.LinkedList;
import java.util.List;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.middleware.Middleware;

abstract public class ControllerWithMiddlewares extends HookController {
    protected List<Middleware> middlewares;

    public ControllerWithMiddlewares() {
        super();
        this.middlewares = new LinkedList<>();
    }

    protected void withMiddlewae(Middleware middleware) {
        this.middlewares.add(middleware);
    }

    @Override
    protected CommandResult beforeAction(Command command) throws Exception {
        for (Middleware middleware : this.middlewares) {
            CommandResult result = middleware.beforeAction(command);
            if (result != null) {
                return result;
            }
        }

        return super.beforeAction(command);
    }

    @Override
    protected CommandResult afterAction(CommandResult result) throws Exception {
        for (Middleware middleware : this.middlewares) {
            result = middleware.afterAction(result);
        }
        return super.afterAction(result);
    }
}
