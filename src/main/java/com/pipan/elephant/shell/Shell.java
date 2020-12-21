package com.pipan.elephant.shell;

public interface Shell {
    public boolean run(String cmd);
    public boolean runWithException(String cmd) throws Exception;
}
