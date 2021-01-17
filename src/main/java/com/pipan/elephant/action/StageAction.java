package com.pipan.elephant.action;

import java.util.Arrays;

import com.pipan.elephant.cleaner.UnusedStageCleaner;
import com.pipan.elephant.generator.IncrementalDirectoryGenerator;
import com.pipan.elephant.hook.Hook;
import com.pipan.elephant.hook.HookChain;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.receipt.Receipt;
import com.pipan.elephant.repository.Repository;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.hook.FileHook;
import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.config.ElephantConfigFactory;
import com.pipan.elephant.source.Upgrader;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.elephant.workingdir.WorkingDirectory;
import com.pipan.filesystem.Directory;
import com.pipan.filesystem.Filesystem;

public class StageAction {
    protected UpgraderRepository upgraderRepository;
    protected Shell shell;
    protected Logger logger;
    protected Repository<Receipt> receiptRepo;

    public StageAction(UpgraderRepository upgraderRepository, Shell shell, Logger logger, Repository<Receipt> receiptRepo) {
        this.upgraderRepository = upgraderRepository;
        this.shell = shell;
        this.logger = logger;
        this.receiptRepo = receiptRepo;
    }

    public void stage(WorkingDirectory workingDirectory) throws Exception {
        ElephantConfig config = (new ElephantConfigFactory()).create(workingDirectory.getConfigFile());
        Upgrader upgrader = this.upgraderRepository.get(config.getSourceType());
        if (upgrader == null) {
            throw new StageException("Invalid source: unknown source type " + config.getSourceType());
        }

        Receipt receipt = null;
        String receiptName = config.getReceipt();
        if (receiptName != null && !receiptName.isEmpty()) {
            receipt = this.receiptRepo.get(config.getReceipt());
            if (receipt == null) {
                this.logger.warning("Receipt unknown '" + receiptName + "'");
            }
        }

        ActionHooks actionHooks = new ActionHooks("stage", this.shell, logger, receipt);

        actionHooks.dispatchBefore(workingDirectory);
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
        actionHooks.dispatchAfter(workingDirectory);
    }
}