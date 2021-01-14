package com.pipan.elephant.workingdir;

import com.pipan.cli.command.Command;
import com.pipan.elephant.Resource;
import com.pipan.filesystem.Filesystem;
import com.pipan.filesystem.FilesystemFactory;

import org.json.JSONObject;

public class WorkingDirectoryFactory {
    private FilesystemFactory filesystemFactory;

    public WorkingDirectoryFactory(FilesystemFactory filesystemFactory) {
        this.filesystemFactory = filesystemFactory;
    }

    public WorkingDirectory create(Command command) throws Exception {
        String currentDirPath = System.getProperty("user.dir");
        String workingDirPath = command.getInput("--d", currentDirPath);
        Filesystem filesystem = this.filesystemFactory.create(workingDirPath);
        
        JSONObject schema = new JSONObject(Resource.getContent("fallback/filesystem.json"));
        
        return new SimpleWorkingDirectory(
            filesystem.getFile(schema.getString("elephant")),
            filesystem.getSymbolicLink(schema.getString("production_link")),
            filesystem.getSymbolicLink(schema.getString("stage_link")),
            filesystem.getDirectory(schema.getString("releases")),
            filesystem.getDirectory(schema.getString("public"))
        );
    }
}
