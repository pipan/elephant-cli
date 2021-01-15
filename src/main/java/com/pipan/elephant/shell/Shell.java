package com.pipan.elephant.shell;

public interface Shell {
    public void out(String message);
    public void err(String message);
    public boolean run(String cmd);
    public boolean runWithException(String cmd) throws Exception;
}
