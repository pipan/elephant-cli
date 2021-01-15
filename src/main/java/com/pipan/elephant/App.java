package com.pipan.elephant;

import com.pipan.cli.Kernel;
import com.pipan.cli.command.Command;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.shell.SimpleShell;
import com.pipan.filesystem.FilesystemFactory;
import com.pipan.filesystem.DiskFilesystemFactory;

public class App 
{
    public static void main( String[] args )
    {
        FilesystemFactory filesystemFactory = new DiskFilesystemFactory();
        Shell shell = new SimpleShell();

        Kernel kernel = new Kernel(new AppBootstrap(filesystemFactory, shell));
        Command command = Command.fromArgs(args);

        kernel.run(command);
    }
}
