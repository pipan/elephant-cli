package com.pipan.cli.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CommandArgumentTest {
    @Test
    public void testCreateFromStringWithEmptyString()
    {
        CommandArgument argument = CommandArgument.fromString("");
        assertEquals("", argument.getName());
        assertEquals("", argument.getValue());
    }

    @Test
    public void testCreateFromStringWithSwitch()
    {
        CommandArgument argument = CommandArgument.fromString("--switch");
        assertEquals("--switch", argument.getName());
        assertEquals("", argument.getValue());
    }

    @Test
    public void testCreateFromStringWithKeyValue()
    {
        CommandArgument argument = CommandArgument.fromString("key=value");
        assertEquals("key", argument.getName());
        assertEquals("value", argument.getValue());
    }

    @Test
    public void testCreateFromStringWithKeyValueMultipleWords()
    {
        CommandArgument argument = CommandArgument.fromString("key=value with more words");
        assertEquals("key", argument.getName());
        assertEquals("value with more words", argument.getValue());
    }

    @Test
    public void testCreateFromStringWithKeyValueMultipleEqualsSigns()
    {
        CommandArgument argument = CommandArgument.fromString("key=value=with=more=words");
        assertEquals("key", argument.getName());
        assertEquals("value=with=more=words", argument.getValue());
    }
}
