package com.pipan.elephant.action;

import com.pipan.elephant.cleaner.UnusedStageCleaner;
import com.pipan.elephant.generator.IncrementalDirectoryGenerator;
import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.config.ElephantConfigFactory;
import com.pipan.elephant.source.Upgrader;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Directory;

public class StageAction {
    protected UpgraderRepository upgraderRepository;

    public StageAction(UpgraderRepository upgraderRepository) {
        this.upgraderRepository = upgraderRepository;
    }

    public void stage(WorkingDirectory workingDirectory) throws Exception {
        ElephantConfig config = (new ElephantConfigFactory()).create(workingDirectory.getConfigFile());
        Upgrader upgrader = this.upgraderRepository.get(config.getSourceType());
        if (upgrader == null) {
            throw new StageException("Invalid source: unknown source type " + config.getSourceType());
        }

        // todo fire before hook
        Directory releaseDir = (new IncrementalDirectoryGenerator(workingDirectory)).next();
        releaseDir.delete();
        
        boolean result = upgrader.upgrade(releaseDir, config);
        if (!result) {
            throw new StageException("Upgrade failed: unknown error");
        }
        // if (!releaseDir.exists()) {
        //     return CommandResult.fail("Upgrade was not successful");
        // }

        workingDirectory.getStageLink().setTarget(releaseDir.getAbsolutePath());
        (new UnusedStageCleaner(workingDirectory)).clean();
        // todo fire after hook
    }
}