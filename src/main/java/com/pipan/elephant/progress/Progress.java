package com.pipan.elephant.progress;

public interface Progress {
    public void print(String message);
    public void info(String message);
    public void error(String message);
    public void exception(Exception ex);
}
