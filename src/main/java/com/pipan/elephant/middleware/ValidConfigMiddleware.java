package com.pipan.elephant.middleware;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.middleware.BaseMiddleware;
import com.pipan.elephant.config.Config;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.source.Upgrader;
import com.pipan.elephant.source.UpgraderRepository;

public class ValidConfigMiddleware extends BaseMiddleware {
    private Releases releases;
    private UpgraderRepository upgraderRepository;

    public ValidConfigMiddleware(Releases releases, UpgraderRepository upgraderRepository) {
        super();
        this.releases = releases;
        this.upgraderRepository = upgraderRepository;
    }

    @Override
    public CommandResult beforeAction(Command command) throws Exception{
        Config config = this.releases.getConfig();
        String source = config.getString("source");

        Upgrader upgrader = this.upgraderRepository.get(source);
        if (upgrader == null) {
            return CommandResult.fail("Source unknown");
        }
        return super.beforeAction(command);
        
    }
}
