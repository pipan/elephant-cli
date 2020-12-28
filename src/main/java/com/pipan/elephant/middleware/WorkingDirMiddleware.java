package com.pipan.elephant.middleware;

import java.nio.file.Paths;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.middleware.BaseMiddleware;
import com.pipan.elephant.workingdir.ProxyWorkingDirectory;
import com.pipan.elephant.workingdir.SimpleWorkingDirectory;
import com.pipan.filesystem.DiskFilesystem;

public class WorkingDirMiddleware extends BaseMiddleware {
    private ProxyWorkingDirectory workingDir;

    public WorkingDirMiddleware(ProxyWorkingDirectory workingDir)
    {
        super();
        this.workingDir = workingDir;
    }

    @Override
    public CommandResult beforeAction(Command command) throws Exception {
        String currentDirPath = System.getProperty("user.dir");
        String workingDirPath = command.getInput("--d", currentDirPath);
        this.workingDir.set(
            SimpleWorkingDirectory.fromConfig(new DiskFilesystem(Paths.get(workingDirPath)))
        );
        return super.beforeAction(command);
    }
}
