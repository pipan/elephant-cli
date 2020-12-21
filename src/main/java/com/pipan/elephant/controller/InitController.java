package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.File;

import org.json.JSONException;
import org.json.JSONObject;

public class InitController implements Controller {
    private WorkingDirectory workingDirectory;

    public InitController(WorkingDirectory workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public CommandResult execute(Command command) throws Exception {
        File config = this.workingDirectory.getConfigFile();

        if (config.exists()) {
            return CommandResult.fail("Config file exists");
        }

        JSONObject json = new JSONObject();
        try {
            json.put("source", "git");
            json.put("history_limit", 5);
            JSONObject git = new JSONObject();
            json.put("git", git);
            git.put("url", "");
            git.put("composer", true);
        } catch (JSONException ex) {
            return CommandResult.fail("Cannot create config file");
        }

        config.writeJson(json);
        this.workingDirectory.getReleasesDirectory().make();
        this.workingDirectory.getPublicDirectory().make();
        this.workingDirectory.getPublicDirectory().enterDir(".well-known").make();

        return CommandResult.ok("Done");
    }
}
