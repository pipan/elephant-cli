package com.pipan.elephant.shell;

import java.io.IOException;

public class SimpleShell implements Shell {
    public boolean run(String cmd) {
        try {
            return this.runWithException(cmd);
        } catch (IOException | InterruptedException ex) {
            return false;
        }
    }

    public boolean runWithException(String cmd) throws IOException, InterruptedException {
        Runtime run = Runtime.getRuntime();
        Process process = run.exec(cmd);
        process.waitFor();
        return true;
    }
}
