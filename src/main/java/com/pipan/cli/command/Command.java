package com.pipan.cli.command;

import java.util.Hashtable;
import java.util.Map;

public class Command 
{
    private String name;
    private Map<String, String> inputs;

    public Command(String name, Map<String, String> inputs)
    {
        this.name = name;
        this.inputs = inputs;
    }

    public Command(String name)
    {
        this(name, new Hashtable<>());
    }

    public static Command fromArgs(String[] args)
    {
        String name = args.length > 0 ? args[0] : "";
        Command command = new Command(name);
        for (int i = 1; i < args.length; i++) {
            CommandArgument argument = CommandArgument.fromString(args[i]);
            command = command.withInput(argument);
        }
        return command;
    }

    public Command withInput(String key, String value)
    {
        Map<String, String> cloneInputs = new Hashtable<>(this.inputs);
        cloneInputs.put(key, value);
        return new Command(this.name, cloneInputs);
    }

    public Command withInput(CommandArgument argument)
    {
        return this.withInput(argument.getName(), argument.getValue());
    }
    
    public String getName()
    {
        return this.name;
    }

    public Boolean hasInput(String key)
    {
        return this.inputs.containsKey(key);
    }

    public String getInput(String key, String def)
    {
        if (!this.hasInput(key)) {
            return def;
        }
        return this.inputs.get(key);
    }

    public Boolean hasSwitch(String name)
    {
        return this.hasInput("--" + name);
    }

    public String getInput(String key)
    {
        return this.getInput(key, null);
    }
}
