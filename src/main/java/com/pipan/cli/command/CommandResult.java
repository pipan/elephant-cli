package com.pipan.cli.command;

public class CommandResult {
    private int code;
    private String message;

    public CommandResult() {
        this("", 0);
    }

    public CommandResult(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public static CommandResult ok(String message) {
        return new CommandResult(message, 0);
    }

    public static CommandResult fail(String message) {
        return new CommandResult(message, 1);
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isOk() {
        return this.code == 0;
    }
}
