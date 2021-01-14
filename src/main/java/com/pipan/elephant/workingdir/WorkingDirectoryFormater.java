package com.pipan.elephant.workingdir;

import java.util.List;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

import com.pipan.elephant.release.ReleaseDirectoryComparator;
import com.pipan.filesystem.Directory;

public class WorkingDirectoryFormater {
    private Comparator<Directory> dirComparator = new ReleaseDirectoryComparator();

    public String format(WorkingDirectory workingDirectory) {
        Collection<? extends Directory> dirs = workingDirectory.getReleasesDirectory().getDirectoryList();
        List<String> lines = new LinkedList<>();
        for (Directory dir : dirs) {
            List<String> tags = this.getTags(workingDirectory, dir);
            lines.add(String.format("%1$-8s %2$s", dir.getName(), String.join(", ", tags)));
        }

        return String.join(System.getProperty("line.separator"), lines);
    }

    private List<String> getTags(WorkingDirectory workingDirectory, Directory dir) {        
        List<String> tags = new LinkedList<>();

        Directory productionDir = workingDirectory.getProductionLink().getTargetDirectory();
        if (this.dirComparator.compare(dir, productionDir) == 0) {
            tags.add("production");
        }

        Directory stageDir = workingDirectory.getStageLink().getTargetDirectory();
        if (this.dirComparator.compare(dir, stageDir) == 0) {
            tags.add("stage");
        }

        return tags;
    }
}