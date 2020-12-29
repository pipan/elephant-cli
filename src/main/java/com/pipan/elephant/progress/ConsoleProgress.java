package com.pipan.elephant.progress;

public class ConsoleProgress implements Progress {
    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public void error(String message) {
        System.err.println("[ Error ] " + message);
    }

    @Override
    public void exception(Exception ex) {
        System.err.println("[ Exception ] " + ex.getMessage());
        ex.printStackTrace();
    }

    @Override
    public void info(String message) {
        this.print("[ Info ] " + message);
    }
}
