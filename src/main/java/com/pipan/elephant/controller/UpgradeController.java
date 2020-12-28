package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.ControllerWithMiddlewares;
import com.pipan.elephant.cleaner.Cleaner;
import com.pipan.elephant.cleaner.RollbackLimitCleaner;
import com.pipan.elephant.config.Config;
import com.pipan.elephant.middleware.InitRequiredMiddleware;
import com.pipan.elephant.progress.Progress;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.service.ApacheService;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.elephant.workingdir.WorkingDirectory;

public class UpgradeController extends ControllerWithMiddlewares {
    private Releases releases;
    private WorkingDirectory workingDirectory;
    private UpgraderRepository upgraderRepository;
    private ApacheService apache;
    private Progress progress;

    public UpgradeController(Releases releases, UpgraderRepository upgraderRepository, ApacheService apache, Progress progress) {
        super();
        this.releases = releases;
        this.workingDirectory = this.releases.getWorkingDirectory();
        this.upgraderRepository = upgraderRepository;
        this.apache = apache;
        this.progress = progress;

        this.withMiddlewae(new InitRequiredMiddleware(this.workingDirectory));
    }

    @Override
    protected CommandResult action(Command command) throws Exception {
        if (!this.releases.isStageAhead()) {
            StageController stage = new StageController(this.releases, this.upgraderRepository);
            CommandResult stageResult = stage.execute(command);
            if (!stageResult.isOk()) {
                return stageResult;
            }
        }

        WorkingDirectory workingDir = this.releases.getWorkingDirectory();
        workingDir.getProductionLink().setTarget(
            workingDir.getStageLink().getTargetDirectory().getAbsolutePath()
        );
        
        this.progress.info("Reloading php fpm");
        this.apache.reloadFpm();

        Config config = this.releases.getConfig();
        Cleaner rollbackLimitCleaner = new RollbackLimitCleaner(workingDir, config.getInteger("history_limit", 5));
        rollbackLimitCleaner.clean();

        return CommandResult.ok("Done");
    }
}
