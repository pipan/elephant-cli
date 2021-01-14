package com.pipan.elephant;

import java.util.Arrays;

import com.pipan.cli.bootstrap.Bootstrap;
import com.pipan.cli.bootstrap.ExceptionHandlerContext;
import com.pipan.cli.bootstrap.MiddlewareContext;
import com.pipan.cli.bootstrap.RouteContext;
import com.pipan.elephant.controller.HelpController;
import com.pipan.elephant.controller.InitController;
import com.pipan.elephant.controller.RollbackController;
import com.pipan.elephant.controller.StageController;
import com.pipan.elephant.controller.StatusController;
import com.pipan.elephant.controller.UpgradeController;
import com.pipan.elephant.service.ApacheService;
import com.pipan.elephant.service.ComposerService;
import com.pipan.elephant.service.GitService;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.shell.SimpleShell;
import com.pipan.elephant.source.composerproject.ComposerProjectUpgrader;
import com.pipan.elephant.source.git.GitUpgrader;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.elephant.workingdir.WorkingDirectoryFactory;
import com.pipan.filesystem.FilesystemFactory;
import com.pipan.elephant.exceptionhandler.PrintExceptionHandler;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.log.SystemLogger;

public class AppBootstrap extends Bootstrap {
    private UpgraderRepository upgraderRepository;
    private Shell shell;
    private FilesystemFactory filesystemFactory;
    private WorkingDirectoryFactory workingDirectoryFactory;
    private Logger logger;

    public AppBootstrap(FilesystemFactory filesystemFactory, Shell shell) {
        this.filesystemFactory = filesystemFactory;
        this.shell = shell;
        this.logger = new SystemLogger();

        this.workingDirectoryFactory = new WorkingDirectoryFactory(this.filesystemFactory);
        this.upgraderRepository = new UpgraderRepository();

        GitService git = new GitService(this.shell);
        ComposerService composer = new ComposerService(this.shell);
        upgraderRepository.add("git", new GitUpgrader(git, composer, this.logger));
        upgraderRepository.add("composer-project", new ComposerProjectUpgrader(composer, this.logger));
    }

    @Override
    public void route(RouteContext context) {
        super.route(context);

        context.addRoute(Arrays.asList("help", "-h", "--help"), new HelpController(this.shell));
        context.addRoute("init", new InitController(this.workingDirectoryFactory, this.shell));
        context.addRoute("stage", new StageController(this.workingDirectoryFactory, this.shell, this.upgraderRepository));
        context.addRoute("upgrade", new UpgradeController(this.workingDirectoryFactory, this.shell, this.upgraderRepository, this.logger));
        context.addRoute("rollback", new RollbackController(this.workingDirectoryFactory, this.shell, this.logger));
        context.addRoute("status", new StatusController(this.workingDirectoryFactory, this.shell));
    }

    @Override
    public void exceptionHandler(ExceptionHandlerContext context) {
        context.addExceptionHandler(new PrintExceptionHandler(this.shell));
    }
}
