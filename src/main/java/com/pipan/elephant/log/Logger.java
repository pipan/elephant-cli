package com.pipan.elephant.log;

public interface Logger {
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARNING = 4;
    public static final int ERROR = 8;

    public void debug(String message);
    public void info(String message);
    public void warning(String message);
    public void error(String message);
}