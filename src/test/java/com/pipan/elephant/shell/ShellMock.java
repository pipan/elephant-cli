package com.pipan.elephant.shell;

import java.util.Hashtable;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;

import org.junit.jupiter.api.Assertions;

public class ShellMock implements Shell{
    private Map<String, Exception> exceptions;
    private Map<String, Boolean> responses;
    private Map<String, Integer> calls;
    private List<String> prints;
    private List<String> errorPrints;

    public ShellMock() {
        this.exceptions = new Hashtable<>();
        this.responses = new Hashtable<>();
        this.calls = new Hashtable<>();
        this.prints = new LinkedList<>();
        this.errorPrints = new LinkedList<>();
    }

    public ShellMock withException(String cmd, Exception ex) {
        this.exceptions.put(cmd, ex);
        return this;
    }

    public ShellMock withResponse(String cmd, Boolean response) {
        this.responses.put(cmd, response);
        return this;
    }

    public Integer getNumberOfCalls(String cmd) {
        if (!this.calls.containsKey(cmd)) {
            return 0;
        }
        return this.calls.get(cmd);
    }

    public ShellMock assertExecuted(String cmd) {
        return this.assertExecuted(cmd, 1);
    }

    public ShellMock assertExecuted(String cmd, int count) {
        Assertions.assertEquals(count, this.getNumberOfCalls(cmd), "Number of command (" + cmd + ") execution does not match");
        return this;
    }

    public ShellMock assertPrintCount(int count) {
        Assertions.assertEquals(count, this.prints.size(), "Number of shell messages is different");
        return this;
    }

    public ShellMock assertPrint(int index, String message) {
        Assertions.assertNotNull(this.prints.get(index), "Shell does not contain message with this index");
        Assertions.assertEquals(message, this.prints.get(index), "Shell message is different");
        return this;
    }

    public ShellMock assertPrintErrorCount(int count) {
        Assertions.assertEquals(count, this.errorPrints.size(), "Number of error shell messages is different");
        return this;
    }

    public ShellMock assertPrintError(int index, String message) {
        Assertions.assertNotNull(this.errorPrints.get(index), "Shell does not contain error message with this index");
        Assertions.assertEquals(message, this.errorPrints.get(index), "Shell error message is different");
        return this;
    }

    @Override
    public void out(String message) {
        this.prints.add(message);
    }

    @Override
    public void err(String message) {
        this.errorPrints.add(message);
    }

    @Override
    public boolean run(String cmd) {
        try {
            return this.runWithException(cmd);
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean runWithException(String cmd) throws Exception {
        this.calls.put(cmd, this.calls.getOrDefault(cmd, 0) + 1);

        if (this.exceptions.containsKey(cmd)) {
            throw this.exceptions.get(cmd);
        }

        if (!this.responses.containsKey(cmd)) {
            return true;
        }
        return this.responses.get(cmd);
    }
}
