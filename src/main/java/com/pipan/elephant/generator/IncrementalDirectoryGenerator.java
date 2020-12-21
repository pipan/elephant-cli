package com.pipan.elephant.generator;

import com.pipan.elephant.workingdir.WorkingDirectory;
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
        if (stageDir == null) {
            return releases.enterDir("1");
        }
        Integer dirInt = Integer.parseInt(stageDir.getName()) + 1;
        return releases.enterDir(dirInt.toString());
    }
}
