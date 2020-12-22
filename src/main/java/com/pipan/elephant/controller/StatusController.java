package com.pipan.elephant.controller;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.elephant.release.ReleaseDirectoryComparator;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Directory;

public class StatusController implements Controller {
    private Releases releases;
    private Comparator<Directory> dirComparator;

    public StatusController(Releases releases) {
        this.releases = releases;
        this.dirComparator = new ReleaseDirectoryComparator();
    }

    @Override
    public CommandResult execute(Command command) throws Exception {
        WorkingDirectory workingDirectory = this.releases.getWorkingDirectory();
        if (!workingDirectory.getReleasesDirectory().exists()) {
            return CommandResult.fail("Releases directory does not exists");
        }

        Collection<? extends Directory> dirs = this.releases.getWorkingDirectory().getReleasesDirectory().getDirectoryList();
        if (dirs.isEmpty()) {
            return CommandResult.fail("Empty releases directory");
        }

        String newLine = System.getProperty("line.separator");
        String status = newLine;
        for (Directory dir : dirs) {
            List<String> responsibilities = this.getResponsibilities(dir);

            status += dir.getName();
            if (!responsibilities.isEmpty()) {
                status += " - " + String.join(", ", responsibilities);
            }
            status += newLine;
        }

        return CommandResult.ok(status);
    }

    private List<String> getResponsibilities(Directory dir) {
        List<String> responsibilities = new LinkedList<>();

        Directory productionDir = this.releases.getProductionDirectory();
        if (this.dirComparator.compare(dir, productionDir) == 0) {
            responsibilities.add("production");
        }

        Directory stageDir = this.releases.getStageDirectory();
        if (this.dirComparator.compare(dir, stageDir) == 0) {
            responsibilities.add("stage");
        }

        return responsibilities;
    }
}
