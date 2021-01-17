package com.pipan.elephant.controller;

import java.util.Arrays;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.ControllerWithMiddlewares;
import com.pipan.elephant.action.ActionHooks;
import com.pipan.elephant.action.StageAction;
import com.pipan.elephant.cleaner.RollbackLimitCleaner;
import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.config.ElephantConfigFactory;
import com.pipan.elephant.hook.Hook;
import com.pipan.elephant.hook.HookChain;
import com.pipan.elephant.hook.FileHook;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.middleware.ValidateElephantFileMiddleware;
import com.pipan.elephant.receipt.Receipt;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.repository.Repository;
import com.pipan.elephant.service.ApacheService;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.elephant.workingdir.WorkingDirectoryFactory;
import com.pipan.filesystem.Filesystem;

public class UpgradeController extends ControllerWithMiddlewares {
    private WorkingDirectoryFactory workingDirectoryFactory;
    private Shell shell;
    private Logger logger;
    private ApacheService apache;
    private StageAction stageAction;
    private ActionHooks actionHooks;

    public UpgradeController(WorkingDirectoryFactory workingDirectoryFactory, Shell shell, UpgraderRepository upgraderRepository, Logger logger, Repository<Receipt> receiptRepo) {
        super();
        this.workingDirectoryFactory = workingDirectoryFactory;
        this.shell = shell;
        this.apache = new ApacheService(shell);
        this.logger = logger;
        this.stageAction = new StageAction(upgraderRepository, this.shell, this.logger, receiptRepo);
        this.actionHooks = new ActionHooks("upgrade", this.shell, this.logger);

        this.withMiddleware(new ValidateElephantFileMiddleware(this.workingDirectoryFactory, this.shell));
    }

    @Override
    protected CommandResult action(Command command) throws Exception {
        WorkingDirectory workingDirectory = this.workingDirectoryFactory.create(command);
        Releases releases = new Releases(workingDirectory);

        if (!releases.isStageAhead()) {
            this.logger.info("Stage new version");
            this.stageAction.stage(workingDirectory);
            this.logger.info("Stage new version: done");
        }

        this.actionHooks.dispatchBefore(workingDirectory);

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

        this.actionHooks.dispatchAfter(workingDirectory);

        this.shell.out("Upgrade successful");
        return CommandResult.ok();
    }
}
