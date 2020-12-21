package com.pipan.elephant.command;

import com.pipan.cli.command.CommandResult;

import org.junit.jupiter.api.Assertions;

public class AssertableCommandResult {
    private CommandResult result;

    public AssertableCommandResult(CommandResult result) {
        this.result = result;
    }

    public int getCode() {
        return this.result.getCode();
    }

    public boolean isOk() {
        return this.result.isOk();
    }

    public String getMessage() {
        return this.result.getMessage();
    }

    public AssertableCommandResult assertEquals(int expectedCode, String expectedMessage) {
        Assertions.assertEquals(expectedCode, this.getCode());
        Assertions.assertEquals(expectedMessage, this.getMessage());
        return this;
    }

    public AssertableCommandResult assertOk(String expectedMessage) {
        return this.assertEquals(0, expectedMessage);
    }

    public AssertableCommandResult assertOk() {
        return this.assertOk("Done");
    }

    public AssertableCommandResult assertFailed(String expectedMessage) {
        return this.assertEquals(1, expectedMessage);
    }
}
