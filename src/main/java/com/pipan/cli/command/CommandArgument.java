package com.pipan.cli.command;

public class CommandArgument {
    private String name;
    private String value;

    public CommandArgument(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public static CommandArgument fromString(String item)
    {
        String[] parts = item.split("=");
        String value = "";
        for (int j = 1; j < parts.length; j++) {
            if (value.length() > 0) {
                value += "=";
            }
            value += parts[j];
        }
        return new CommandArgument(parts[0], value);
    }

    public String getName()
    {
        return this.name;
    }

    public String getValue()
    {
        return this.value;
    }
}
