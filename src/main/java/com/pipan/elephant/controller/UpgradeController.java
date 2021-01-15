package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.ControllerWithMiddlewares;
import com.pipan.elephant.action.StageAction;
import com.pipan.elephant.cleaner.RollbackLimitCleaner;
import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.config.ElephantConfigFactory;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.middleware.ValidateElephantFileMiddleware;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.service.ApacheService;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.elephant.workingdir.WorkingDirectoryFactory;

public class UpgradeController extends ControllerWithMiddlewares {
    private WorkingDirectoryFactory workingDirectoryFactory;
    private Shell shell;
    private UpgraderRepository upgraderRepository;
    private Logger logger;
    private ApacheService apache;

    public UpgradeController(WorkingDirectoryFactory workingDirectoryFactory, Shell shell, UpgraderRepository upgraderRepository, Logger logger) {
        super();
        this.workingDirectoryFactory = workingDirectoryFactory;
        this.shell = shell;
        this.upgraderRepository = upgraderRepository;
        this.apache = new ApacheService(shell);
        this.logger = logger;

        this.withMiddleware(new ValidateElephantFileMiddleware(this.workingDirectoryFactory, this.shell));
    }

    @Override
    protected CommandResult action(Command command) throws Exception {
        WorkingDirectory workingDirectory = this.workingDirectoryFactory.create(command);
        Releases releases = new Releases(workingDirectory);

        if (!releases.isStageAhead()) {
            this.logger.info("Stage new version");
            (new StageAction(this.upgraderRepository)).stage(workingDirectory);
            this.logger.info("Stage new version: done");
        }

        this.logger.info("Set production link");
        workingDirectory.getProductionLink().setTarget(
            workingDirectory.getStageLink().getTargetDirectory().getAbsolutePath()
        );
        this.logger.info("Set production link: done");
        
        this.logger.info("Reloading php fpm");
        this.apache.reloadFpm();
        this.logger.info("Reloading php fpm: done");

        this.logger.info("Remove unused upgrades");
        ElephantConfig config = (new ElephantConfigFactory()).create(workingDirectory.getConfigFile());
        (new RollbackLimitCleaner(workingDirectory, config.getHistoryLimit())).clean();
        this.logger.info("Remove unused upgrades: done");

        this.shell.out("Upgrade successful");
        return CommandResult.ok();
    }
}
