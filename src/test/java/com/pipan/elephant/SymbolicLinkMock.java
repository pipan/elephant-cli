package com.pipan.elephant;

import com.pipan.filesystem.Directory;
import com.pipan.filesystem.SymbolicLink;

public class SymbolicLinkMock implements SymbolicLink {
    private String target;

    public SymbolicLinkMock(String target) {
        this.target = target;
    }

    public SymbolicLinkMock() {
        this(null);
    }

    @Override
    public boolean exists() {
        return this.target != null;
    }

    @Override
    public Directory getTargetDirectory() {
        if (this.target == null) {
            return null;
        }
        return new DirectoryMock(this.target, true);
    }

    @Override
    public String getTargetString() {
        return this.target;
    }

    @Override
    public void setTarget(String targetPath) {
        this.target = targetPath;
    }
}
