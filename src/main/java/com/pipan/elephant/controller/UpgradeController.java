package com.pipan.elephant.controller;

import java.util.Arrays;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.ControllerWithMiddlewares;
import com.pipan.elephant.action.ActionHooks;
import com.pipan.elephant.action.FpmAction;
import com.pipan.elephant.action.StageAction;
import com.pipan.elephant.cleaner.RollbackLimitCleaner;
import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.config.ElephantConfigFactory;
import com.pipan.elephant.hook.Hook;
import com.pipan.elephant.hook.HookChain;
import com.pipan.elephant.hook.FileHook;
import com.pipan.elephant.middleware.ValidateElephantFileMiddleware;
import com.pipan.elephant.output.ConsoleOutput;
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
    private FpmAction fpmAction;
    private StageAction stageAction;
    private ActionHooks actionHooks;
    private ConsoleOutput output;

    public UpgradeController(WorkingDirectoryFactory workingDirectoryFactory, Shell shell, UpgraderRepository upgraderRepository, Repository<Receipt> receiptRepo, ConsoleOutput output) {
        super();
        this.workingDirectoryFactory = workingDirectoryFactory;
        this.shell = shell;
        this.output = output;
        this.fpmAction = new FpmAction(new ApacheService(shell), this.output);
        this.stageAction = new StageAction(upgraderRepository, this.shell, receiptRepo);
        this.actionHooks = new ActionHooks("upgrade", this.shell, this.output);

        this.withMiddleware(new ValidateElephantFileMiddleware(this.workingDirectoryFactory, this.shell));
    }

    @Override
    protected CommandResult action(Command command) throws Exception {
        WorkingDirectory workingDirectory = this.workingDirectoryFactory.create(command);
        Releases releases = new Releases(workingDirectory);

        if (!releases.isStageAhead()) {
            this.stageAction.stage(workingDirectory);
        }

        this.actionHooks.dispatchBefore(workingDirectory);

        this.output.write("[...] Activate next version");
        workingDirectory.getProductionLink().setTarget(
            workingDirectory.getStageLink().getTargetDirectory().getAbsolutePath()
        );
        this.output.rewrite("[<green> ✔️ </green>] Activate next version");

        ElephantConfig config = (new ElephantConfigFactory()).create(workingDirectory.getConfigFile());
        
        this.fpmAction.run(config);

        this.output.write("[...] Remove unused upgrades");
        (new RollbackLimitCleaner(workingDirectory, config.getHistoryLimit())).clean();
        this.output.rewrite("[<green> ✔️ </green>] Remove unused upgrades");

        this.actionHooks.dispatchAfter(workingDirectory);

        this.output.write("<green>Upgrade successful</green>");
        return CommandResult.ok();
    }
}
