package com.pipan.filesystem;

import java.util.Collection;

public interface Directory {
    public boolean exists();
    public String getAbsolutePath();
    public String getName();
    public Collection<? extends Directory> getDirectoryList();
    public void make();
    public void delete();
    public Directory enterDir(String name);
    public Directory leaveDir();
}
