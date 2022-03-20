package com.pipan.elephant.shell;

import java.util.List;

public interface Shell {
    public void out(String message);
    public void err(String message);
    public boolean run(String cmd);
    public void runWithException(String cmd) throws Exception;
    public void runWithException(String... cmd) throws Exception;
    public void runWithException(List<String> cmd) throws Exception;
}
