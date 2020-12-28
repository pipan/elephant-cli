package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.ControllerWithMiddlewares;
import com.pipan.elephant.cleaner.Cleaner;
import com.pipan.elephant.cleaner.UnusedStageCleaner;
import com.pipan.elephant.config.Config;
import com.pipan.elephant.generator.Generator;
import com.pipan.elephant.generator.IncrementalDirectoryGenerator;
import com.pipan.elephant.middleware.InitRequiredMiddleware;
import com.pipan.elephant.middleware.StageAheadMiddleware;
import com.pipan.elephant.middleware.ValidConfigMiddleware;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.source.Upgrader;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Directory;

public class StageController extends ControllerWithMiddlewares {
    private Releases releases;
    private WorkingDirectory workingDirectory;
    private UpgraderRepository upgraderRepository;
    private Generator<Directory> generator;
    private Cleaner stageCleaner;

    public StageController(Releases releases, UpgraderRepository upgraderRepository) {
        super();
        this.releases = releases;
        this.workingDirectory = this.releases.getWorkingDirectory();
        this.generator = new IncrementalDirectoryGenerator(this.workingDirectory);
        this.upgraderRepository = upgraderRepository;
        this.stageCleaner = new UnusedStageCleaner(this.workingDirectory);

        this.withMiddlewae(new InitRequiredMiddleware(this.workingDirectory));
        this.withMiddlewae(new StageAheadMiddleware(this.releases));
        this.withMiddlewae(new ValidConfigMiddleware(this.releases, this.upgraderRepository));
    }

    @Override
    protected CommandResult action(Command command) throws Exception {
        Config config = this.releases.getConfig();
        String source = config.getString("source");

        Directory releaseDir = this.generator.next();
        releaseDir.delete();

        Upgrader upgrader = this.upgraderRepository.get(source);
        boolean result = upgrader.upgrade(releaseDir, this.releases.getSourceConfig());
        if (!result) {
            return CommandResult.fail("Upgrade failed");
        }
        if (!releaseDir.exists()) {
            return CommandResult.fail("Upgrade was not successful");
        }

        this.workingDirectory.getStageLink().setTarget(releaseDir.getAbsolutePath());
        this.stageCleaner.clean();
        
        return CommandResult.ok("Done");
    }
}
