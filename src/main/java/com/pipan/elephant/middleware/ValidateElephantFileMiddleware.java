package com.pipan.elephant.middleware;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.middleware.BaseMiddleware;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.elephant.workingdir.WorkingDirectoryFactory;
import com.pipan.filesystem.File;
import com.pipan.filesystem.ReadException;
import com.pipan.filesystem.json.JsonFile;

import org.json.JSONObject;

public class ValidateElephantFileMiddleware extends BaseMiddleware {
    protected WorkingDirectoryFactory workingDirectoryFactory;
    protected Shell shell;

    public ValidateElephantFileMiddleware(WorkingDirectoryFactory workingDirectoryFactory, Shell shell) {
        super();
        this.workingDirectoryFactory = workingDirectoryFactory;
        this.shell = shell;
    }

    @Override
    public CommandResult beforeAction(Command command) throws Exception {
        WorkingDirectory workingDirectory = this.workingDirectoryFactory.create(command);
        File config = workingDirectory.getConfigFile();

        if (!config.exists()) {
            this.shell.err("Elephant file is missing");
            return CommandResult.fail("Elephant file is missing");
        }

        JsonFile jsonFile = new JsonFile(config);
        try {
            JSONObject json = jsonFile.read();
        } catch (ReadException ex) {
            this.shell.err("Elephant file is invalid");
            return CommandResult.fail("Elephant file is invalid");
        }

        return super.beforeAction(command);
    }
}
