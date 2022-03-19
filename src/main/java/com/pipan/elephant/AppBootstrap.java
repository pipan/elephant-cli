package com.pipan.elephant;

import java.util.Arrays;

import com.pipan.cli.bootstrap.Bootstrap;
import com.pipan.cli.bootstrap.ExceptionHandlerContext;
import com.pipan.cli.bootstrap.MiddlewareContext;
import com.pipan.cli.bootstrap.RouteContext;
import com.pipan.elephant.archive.ArchiveService;
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
import com.pipan.elephant.source.githubrelease.GithubReleaseUpgrader;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.elephant.workingdir.WorkingDirectoryFactory;
import com.pipan.filesystem.FilesystemFactory;
import com.pipan.elephant.exceptionhandler.PrintExceptionHandler;
import com.pipan.elephant.github.GithubApi;
import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.receipt.Receipt;
import com.pipan.elephant.receipt.laravel.LaravelReceipt;
import com.pipan.elephant.repository.MapRepository;
import com.pipan.elephant.repository.Repository;

public class AppBootstrap extends Bootstrap {
    private UpgraderRepository upgraderRepository;
    private Repository<Receipt> receiptRepo;
    private Shell shell;
    private FilesystemFactory filesystemFactory;
    private WorkingDirectoryFactory workingDirectoryFactory;
    private ConsoleOutput output;

    public AppBootstrap(FilesystemFactory filesystemFactory, Shell shell) {
        this.filesystemFactory = filesystemFactory;
        this.shell = shell;
        this.output = new ConsoleOutput();

        this.workingDirectoryFactory = new WorkingDirectoryFactory(this.filesystemFactory);
        this.upgraderRepository = new UpgraderRepository();
        this.receiptRepo = new MapRepository();

        GitService git = new GitService(this.shell);
        ComposerService composer = new ComposerService(this.shell);
        GithubApi githubApi = new GithubApi();
        ArchiveService archiveService = new ArchiveService();
        upgraderRepository.add("git", new GitUpgrader(git, composer));
        upgraderRepository.add("composer-project", new ComposerProjectUpgrader(composer));
        upgraderRepository.add("github-release", new GithubReleaseUpgrader(this.output, githubApi, archiveService));

        receiptRepo.add("laravel", new LaravelReceipt());
    }

    @Override
    public void route(RouteContext context) {
        context.addRoute(Arrays.asList("help", "-h", "--help"), new HelpController(this.shell, this.output));
        context.addRoute("init", new InitController(this.workingDirectoryFactory, this.output));
        context.addRoute("stage", new StageController(this.workingDirectoryFactory, this.shell, this.upgraderRepository, this.receiptRepo, this.output));
        context.addRoute("upgrade", new UpgradeController(this.workingDirectoryFactory, this.shell, this.upgraderRepository, this.receiptRepo, this.output));
        context.addRoute("rollback", new RollbackController(this.workingDirectoryFactory, this.shell, this.output));
        context.addRoute("status", new StatusController(this.workingDirectoryFactory, this.shell));
    }

    @Override
    public void exceptionHandler(ExceptionHandlerContext context) {
        context.addExceptionHandler(new PrintExceptionHandler(this.shell));
    }
}
