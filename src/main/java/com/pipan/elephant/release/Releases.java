package com.pipan.elephant.release;

import java.util.Collection;
import java.util.Comparator;

import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Directory;
import com.pipan.filesystem.ReadException;

import org.json.JSONObject;

public class Releases {
    private WorkingDirectory workingDirectory;
    private Comparator<Directory> dirComparator;

    public Releases(WorkingDirectory workingDirectory) {
        this.workingDirectory = workingDirectory;
        this.dirComparator = new ReleaseDirectoryComparator();
    }

    public WorkingDirectory getWorkingDirectory() {
        return this.workingDirectory;
    }

    public Directory getStageDirectory() {
        return this.workingDirectory.getStageLink().getTargetDirectory();
    }

    public Directory getProductionDirectory() {
        return this.workingDirectory.getProductionLink().getTargetDirectory();
    }

    public Directory getPreviousDirectory() {
        return this.getPreviousDirectory(this.getProductionDirectory());
    }

    public Directory getPreviousDirectory(Directory dir) {
        Collection<? extends Directory> dirs = this.workingDirectory.getReleasesDirectory().getDirectoryList();
        Directory previous = null;
        for (Directory directory : dirs) {
            if (this.dirComparator.compare(directory, dir) < 0 && this.dirComparator.compare(directory, previous) > 0) {
                previous = directory;
            }
        }
        return previous;
    }

    public boolean hasStage() {
        return this.getStageDirectory() != null && this.compareStageProduction() > 0;
    }

    public boolean isStageAhead() {
        return this.compareStageProduction() > 0;
    }

    public boolean isProductionAhead() {
        return this.compareStageProduction() < 0;
    }

    public boolean stageEqualsProduction() {
        return this.compareStageProduction() == 0;
    }

    private int compareStageProduction() {
        Directory stageDir = this.getStageDirectory();
        Directory productionDir = this.getProductionDirectory();

        return this.dirComparator.compare(stageDir, productionDir);
    }
}
