package com.pipan.elephant.workingdir;

import com.pipan.filesystem.Directory;
import com.pipan.filesystem.File;
import com.pipan.filesystem.Filesystem;
import com.pipan.filesystem.SymbolicLink;

public class SimpleWorkingDirectory implements WorkingDirectory {
    private File config;
    private SymbolicLink stage;
    private SymbolicLink production;
    private Directory releases;
    private Directory publicDir;

    public SimpleWorkingDirectory(File config, SymbolicLink production, SymbolicLink stage, Directory releases, Directory publicDir)
    {
        this.config = config;
        this.stage = stage;
        this.production = production;
        this.releases = releases;
        this.publicDir = publicDir;
    }

    public static WorkingDirectory fromConfig(Filesystem filesystem)
    {
        String releases = "releases";
        String publicDir = "public";
        String production = "production_link";
        String stage = "stage_link";
        String config = "elephant.json";

        return new SimpleWorkingDirectory(
            filesystem.getFile(config),
            filesystem.getSymbolicLink(production),
            filesystem.getSymbolicLink(stage),
            filesystem.getDirectory(releases),
            filesystem.getDirectory(publicDir)
        );
    }

    @Override
    public File getConfigFile() {
        return this.config;
    }

    @Override
    public SymbolicLink getProductionLink() {
        return this.production;
    }

    @Override
    public Directory getPublicDirectory() {
        return this.publicDir;
    }

    @Override
    public Directory getReleasesDirectory() {
        return this.releases;
    }

    @Override
    public SymbolicLink getStageLink() {
        return this.stage;
    }
}
