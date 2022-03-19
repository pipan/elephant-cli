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
            this.runWithException(cmd);
        } catch (IOException | InterruptedException ex) {
            return false;
        }
        return true;
    }

    public void runWithException(String... cmd) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process p = pb.start();

        Integer resultCode = p.waitFor();
        String error = new String(p.getErrorStream().readAllBytes(), "UTF-8");
        
        if (resultCode > 0 || error.length() > 0) {
            if (error.endsWith(System.lineSeparator())) {
                error = error.substring(0, error.length() - 1);
            }
            throw new InterruptedException(resultCode.toString() + " " + error);
        }
    }

    public void runWithException(String cmd) throws IOException, InterruptedException {
        this.runWithException(new String[] {cmd});
    }
}
