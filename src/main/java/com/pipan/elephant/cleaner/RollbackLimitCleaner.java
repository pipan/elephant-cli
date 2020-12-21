package com.pipan.elephant.cleaner;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.pipan.elephant.release.ReleaseDirectoryComparator;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Directory;

public class RollbackLimitCleaner implements Cleaner {
    private WorkingDirectory workingDirectory;
    private Comparator<Directory> comparator;
    private Integer limit;

    public RollbackLimitCleaner(WorkingDirectory workingDirectory, Integer limit) {
        this.workingDirectory = workingDirectory;
        this.comparator = new ReleaseDirectoryComparator();
        this.limit = limit;
    }

    @Override
    public void clean() {
        Directory productionDir = this.workingDirectory.getProductionLink().getTargetDirectory();
        List<Directory> rollbacks = new LinkedList<>();
        Collection<? extends Directory> dirs = this.workingDirectory.getReleasesDirectory().getDirectoryList();
        for (Directory dir : dirs) {
            if (this.comparator.compare(dir, productionDir) >= 0) {
                continue;
            }
            rollbacks.add(dir);
        }

        if (rollbacks.size() <= this.limit) {
            return;
        }

        int removeSize = rollbacks.size() - this.limit;
        Collections.sort(rollbacks, this.comparator);
        for (int i = 0; i < removeSize; i++) {
            rollbacks.get(i).delete();
        }
    }
}
