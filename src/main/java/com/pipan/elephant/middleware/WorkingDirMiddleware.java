package com.pipan.elephant.middleware;

import java.nio.file.Paths;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.middleware.Middleware;
import com.pipan.elephant.workingdir.ProxyWorkingDirectory;
import com.pipan.elephant.workingdir.SimpleWorkingDirectory;
import com.pipan.filesystem.DiskFilesystem;

public class WorkingDirMiddleware implements Middleware {
    private ProxyWorkingDirectory workingDir;

    public WorkingDirMiddleware(ProxyWorkingDirectory workingDir)
    {
        this.workingDir = workingDir;
    }

    @Override
    public void afterAction(CommandResult result) {}

    @Override
    public CommandResult beforeAction(Command command) {
        String currentDirPath = System.getProperty("user.dir");
        String workingDirPath = command.getInput("--d", currentDirPath);
        this.workingDir.set(
            SimpleWorkingDirectory.fromConfig(new DiskFilesystem(Paths.get(workingDirPath)))
        );
        return null;
    }
}
