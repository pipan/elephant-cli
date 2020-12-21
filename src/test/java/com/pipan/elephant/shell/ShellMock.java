package com.pipan.elephant.shell;

import java.util.Hashtable;
import java.util.Map;

public class ShellMock implements Shell{
    private Map<String, Exception> exceptions;
    private Map<String, Boolean> responses;
    private Map<String, Integer> calls;

    public ShellMock() {
        this.exceptions = new Hashtable<>();
        this.responses = new Hashtable<>();
        this.calls = new Hashtable<>();
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
