package com.pipan.elephant.workingdir;

import com.pipan.filesystem.Directory;
import com.pipan.filesystem.File;
import com.pipan.filesystem.SymbolicLink;

public interface WorkingDirectory {
    public File getConfigFile();
    public Directory getReleasesDirectory();
    public Directory getPublicDirectory();
    public SymbolicLink getStageLink();
    public SymbolicLink getProductionLink();
}
