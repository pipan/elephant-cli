package com.pipan.elephant;

import com.pipan.cli.Kernel;
import com.pipan.cli.command.Command;

public class App 
{
    public static void main( String[] args )
    {
        Kernel kernel = new Kernel(new AppBootstrap());
        Command command = Command.fromArgs(args);

        kernel.run(command);
    }
}
