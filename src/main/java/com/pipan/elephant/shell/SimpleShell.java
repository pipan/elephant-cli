package com.pipan.elephant.shell;

import java.io.IOException;

public class SimpleShell implements Shell {
    public void out(String message) {
        System.out.println(message);
    }

    public void err(String message) {
        System.err.println(message);
    }

    public boolean run(String cmd) {
        try {
            return this.runWithException(cmd);
        } catch (IOException | InterruptedException ex) {
            return false;
        }
    }

    public boolean runWithException(String... cmd) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        return p.waitFor() == 0;
    }

    public boolean runWithException(String cmd) throws IOException, InterruptedException {
        return this.runWithException(new String[] {cmd});
    }
}
