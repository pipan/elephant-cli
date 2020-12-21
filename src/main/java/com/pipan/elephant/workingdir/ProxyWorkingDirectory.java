package com.pipan.elephant.workingdir;

import com.pipan.filesystem.Directory;
import com.pipan.filesystem.File;
import com.pipan.filesystem.SymbolicLink;

public class ProxyWorkingDirectory implements WorkingDirectory {
    private WorkingDirectory proxy;

    public ProxyWorkingDirectory(WorkingDirectory proxy)
    {
        this.set(proxy);
    }

    public ProxyWorkingDirectory()
    {
        this(null);
    }

    public void set(WorkingDirectory proxy) {
        this.proxy = proxy;
    }

    @Override
    public File getConfigFile() {
        return this.proxy.getConfigFile();
    }

    @Override
    public SymbolicLink getProductionLink() {
        return this.proxy.getProductionLink();
    }

    @Override
    public Directory getPublicDirectory() {
        return this.proxy.getPublicDirectory();
    }

    @Override
    public Directory getReleasesDirectory() {
        return this.proxy.getReleasesDirectory();
    }

    @Override
    public SymbolicLink getStageLink() {
        return this.proxy.getStageLink();
    }
}
