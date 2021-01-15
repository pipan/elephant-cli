package com.pipan.elephant.generator;

import com.pipan.elephant.workingdir.WorkingDirectory;

import java.util.Comparator;

import com.pipan.elephant.release.ReleaseDirectoryComparator;
import com.pipan.filesystem.Directory;

public class IncrementalDirectoryGenerator implements Generator<Directory> {
    private WorkingDirectory workingDirectory;

    public IncrementalDirectoryGenerator(WorkingDirectory workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    @Override
    public Directory next() {
        Directory releases = this.workingDirectory.getReleasesDirectory();
        
        Directory stageDir = this.workingDirectory.getStageLink().getTargetDirectory();
        Directory productionDir = this.workingDirectory.getProductionLink().getTargetDirectory();

        Comparator<Directory> comparator = new ReleaseDirectoryComparator();
        Directory latestDir = comparator.compare(stageDir, productionDir) > -1 ? stageDir : productionDir;

        if (latestDir == null) {
            return releases.enterDir("1");
        }
        Integer dirInt = Integer.parseInt(latestDir.getName()) + 1;
        return releases.enterDir(dirInt.toString());
    }
}
