package com.pipan.cli;

import java.util.List;

import com.pipan.cli.bootstrap.Bootstrap;
import com.pipan.cli.bootstrap.SimpleContext;
import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.cli.exception.ExceptionHandler;
import com.pipan.cli.exception.NotFoundException;
import com.pipan.cli.middleware.Middleware;
import com.pipan.cli.routing.RouteTable;

public class Kernel 
{
    private RouteTable routeTable;
    private List<Middleware> middlewares;
    private List<ExceptionHandler> exceptionHandlers;

    public Kernel(Bootstrap bootstrap)
    {
        SimpleContext context = new SimpleContext();

        bootstrap.middleware(context);
        bootstrap.route(context);
        bootstrap.exceptionHandler(context);

        this.middlewares = context.getMiddlewares();
        this.routeTable = new RouteTable(context.getRoutes());
        this.exceptionHandlers = context.getExceptionHandlers();
    }

    public CommandResult run(Command command) {
        try {
            CommandResult result = null;
            for (Middleware middleware : this.middlewares) {
                result = middleware.beforeAction(command);
                if (result != null) {
                    break;
                }
            }
            if (result == null) {
                result = this.handleCommand(command);
            }
            
            for (Middleware middleware : this.middlewares) {
                middleware.afterAction(result);
            }

            return result;
        } catch (Exception ex) {
            for (ExceptionHandler handler : this.exceptionHandlers) {
                handler.handle(ex);
            }
            return CommandResult.fail(ex.getMessage());
        }
    }

    public CommandResult handleCommand(Command command) throws Exception {
        Controller controller = this.routeTable.getController(command.getName());
        if (controller == null) {
            throw new NotFoundException("Command not found: " + command.getName());
        }
        return controller.execute(command);
    }

    public void handleException(Exception ex) {
        System.err.println("[ Exception ] " + ex.getMessage());
        ex.printStackTrace();
    }
}
