package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.elephant.cleaner.Cleaner;
import com.pipan.elephant.cleaner.UnusedStageCleaner;
import com.pipan.elephant.config.Config;
import com.pipan.elephant.generator.Generator;
import com.pipan.elephant.generator.IncrementalDirectoryGenerator;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.source.Upgrader;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.filesystem.Directory;

public class StageController extends RequireInitController {
    private Releases releases;
    private UpgraderRepository upgraderRepository;
    private Generator<Directory> generator;
    private Cleaner stageCleaner;

    public StageController(Releases releases, UpgraderRepository upgraderRepository) {
        super(releases.getWorkingDirectory());
        this.releases = releases;
        this.generator = new IncrementalDirectoryGenerator(this.workingDir);
        this.upgraderRepository = upgraderRepository;
        this.stageCleaner = new UnusedStageCleaner(this.workingDir);
    }

    public CommandResult doExecute(Command command) throws Exception {
        Config config = this.releases.getConfig();
        String source = config.getString("source");

        if (this.releases.isProductionAhead()) {
            return CommandResult.fail("Cannot create new stage: stage is behind production");
        }

        Directory releaseDir = this.generator.next();
        releaseDir.delete();

        Upgrader upgrader = this.upgraderRepository.get(source);
        if (upgrader == null) {
            return CommandResult.fail("Source unknown");
        }
        
        boolean result = upgrader.upgrade(releaseDir, this.releases.getSourceConfig());
        if (!result) {
            return CommandResult.fail("Upgrade failed");
        }
        if (!releaseDir.exists()) {
            return CommandResult.fail("Upgrade was not successful");
        }

        this.workingDir.getStageLink().setTarget(releaseDir.getAbsolutePath());
        this.stageCleaner.clean();
        
        return CommandResult.ok("Done");
    }
}
