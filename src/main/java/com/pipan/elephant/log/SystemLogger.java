package com.pipan.elephant.log;

import com.pipan.elephant.shell.Shell;

public class SystemLogger implements Logger {
    protected int printFlag = 12;

    public SystemLogger(int level) {
        this.printFlag = level;
    }

    public SystemLogger() {
        this(12);
    }

    public void enableLevel(int level) {
        this.printFlag = this.printFlag | level;
    }

    public void enableLevelAll() {
        this.printFlag = 15;
    }

    public void disableLevel(int level) {
        this.printFlag = this.printFlag & (level ^ 15);
    }

    public void log(int level, String message) {
        if ((level & this.printFlag) != level) {
            return;
        }
        System.out.println(message);
    }

    public void debug(String message) {
        this.log(Logger.DEBUG, message);
    }

    public void info(String message) {
        this.log(Logger.INFO, message);
    }

    public void warning(String message) {
        this.log(Logger.WARNING, message);
    }

    public void error(String message) {
        this.log(Logger.ERROR, message);
    }
}