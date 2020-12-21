package com.pipan.filesystem;

public interface SymbolicLink {
    public boolean exists();
    public void setTarget(String targetPath);
    public String getTargetString();
    public Directory getTargetDirectory();
}
