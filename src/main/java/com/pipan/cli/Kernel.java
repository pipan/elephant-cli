package com.pipan.cli;

import java.util.List;

import com.pipan.cli.bootstrap.Bootstrap;
import com.pipan.cli.bootstrap.SimpleContext;
import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.cli.middleware.Middleware;
import com.pipan.cli.routing.RouteTable;

public class Kernel 
{
    private RouteTable routeTable;
    private List<Middleware> middlewares;

    public Kernel(Bootstrap bootstrap)
    {
        SimpleContext context = new SimpleContext();

        bootstrap.middleware(context);
        bootstrap.route(context);

        this.middlewares = context.getMiddlewares();
        this.routeTable = new RouteTable(context.getRoutes());
    }

    public void run(Command command) {
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

            this.handleResult(result);
        } catch (Exception ex) {
            this.handleException(ex);
        }
    }

    public CommandResult handleCommand(Command command) throws Exception {
        Controller controller = this.routeTable.getController(command.getName());
        return controller.execute(command);
    }

    public void handleResult(CommandResult result) {
        String label = "";
        if (result.isOk()) {
            label = "[ Done ] ";
        } else {
            label = "[ Error ] ";
        }
        System.out.println(label + result.getMessage());
    }

    public void handleException(Exception ex) {
        System.out.println("[ Exception ] " + ex.getMessage());
        ex.printStackTrace();
    }
}
