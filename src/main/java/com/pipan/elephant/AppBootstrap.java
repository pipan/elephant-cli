package com.pipan.elephant;

import java.util.Arrays;

import com.pipan.cli.bootstrap.Bootstrap;
import com.pipan.cli.bootstrap.MiddlewareContext;
import com.pipan.cli.bootstrap.RouteContext;
import com.pipan.elephant.controller.HelpController;
import com.pipan.elephant.controller.InitController;
import com.pipan.elephant.controller.RollbackController;
import com.pipan.elephant.controller.StageController;
import com.pipan.elephant.controller.StatusController;
import com.pipan.elephant.controller.UpgradeController;
import com.pipan.elephant.middleware.StageAheadMiddleware;
import com.pipan.elephant.middleware.WorkingDirMiddleware;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.service.ApacheService;
import com.pipan.elephant.service.ComposerService;
import com.pipan.elephant.service.GitService;
import com.pipan.elephant.shell.Shell;
import com.pipan.elephant.shell.SimpleShell;
import com.pipan.elephant.source.ComposerProjectUpgrader;
import com.pipan.elephant.source.GitUpgrader;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.elephant.workingdir.ProxyWorkingDirectory;

public class AppBootstrap extends Bootstrap {
    private ProxyWorkingDirectory workingDirectory;
    private Releases releases;
    private UpgraderRepository upgraderRepository;
    private Shell shell;
    private ApacheService apache;
    private GitService git;
    private ComposerService composer;

    public AppBootstrap()
    {
        this.workingDirectory = new ProxyWorkingDirectory();
        this.releases = new Releases(this.workingDirectory);
        this.upgraderRepository = new UpgraderRepository();
        this.shell = new SimpleShell();
        this.apache = new ApacheService(this.shell);
        this.git = new GitService(this.shell);
        this.composer = new ComposerService(this.shell);

        upgraderRepository.add("git", new GitUpgrader(this.git, this.composer));
        upgraderRepository.add("composer-project", new ComposerProjectUpgrader(this.composer));
    }

    @Override
    public void route(RouteContext context)
    {
        super.route(context);

        context.addRoute(Arrays.asList("help", "h", "--help", "--h"), new HelpController());
        context.addRoute("init", new InitController(this.workingDirectory));
        context.addRoute("stage", new StageController(this.releases, this.upgraderRepository));
        context.addRoute("upgrade", new UpgradeController(this.releases, this.upgraderRepository, this.apache));
        context.addRoute("rollback", new RollbackController(this.releases, this.apache));
        context.addRoute("status", new StatusController(this.releases));
    }

    @Override
    public void middleware(MiddlewareContext context) {
        super.middleware(context);

        context.addMiddleware(new WorkingDirMiddleware(this.workingDirectory));
        context.addMiddleware(new StageAheadMiddleware(this.releases));
    }
}
