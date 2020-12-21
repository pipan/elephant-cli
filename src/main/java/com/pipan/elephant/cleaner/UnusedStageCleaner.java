package com.pipan.elephant.cleaner;

import java.util.Collection;
import java.util.Comparator;

import com.pipan.elephant.release.ReleaseDirectoryComparator;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Directory;

public class UnusedStageCleaner implements Cleaner {
    private WorkingDirectory workingDirectory;
    private Comparator<Directory> comparator;

    public UnusedStageCleaner(WorkingDirectory workingDirectory) {
        this.workingDirectory = workingDirectory;
        this.comparator = new ReleaseDirectoryComparator();
    }

    @Override
    public void clean() {
        Directory productionDir = this.workingDirectory.getProductionLink().getTargetDirectory();
        Directory stageDir = this.workingDirectory.getStageLink().getTargetDirectory();
        Collection<? extends Directory> dirs = this.workingDirectory.getReleasesDirectory().getDirectoryList();
        for (Directory dir : dirs) {
            if (this.comparator.compare(dir, productionDir) <= 0 || this.comparator.compare(dir, stageDir) >= 0) {
                continue;
            }
            dir.delete();
        }
    }
}
