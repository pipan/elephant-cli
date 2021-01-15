package com.pipan.filesystem;

import com.pipan.filesystem.Directory;
import com.pipan.filesystem.SymbolicLink;

import org.junit.jupiter.api.Assertions;

public class SymbolicLinkMock implements SymbolicLink {
    private String target;

    public SymbolicLinkMock(String target) {
        this.target = target;
    }

    public SymbolicLinkMock() {
        this(null);
    }

    public void assertTarget(String expectedTarget) throws Exception {
        Assertions.assertEquals(expectedTarget, this.getTargetString(), "SymLink has different target");
    }

    @Override
    public boolean exists() {
        return this.target != null;
    }

    @Override
    public void delete() {
        this.target = null;
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
