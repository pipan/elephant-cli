package com.pipan.elephant.integration;

import com.pipan.cli.command.Command;
import com.pipan.elephant.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelpIntegrationTest extends IntegrationTestCase {
    @Test
    public void testHelpCommand() throws Exception {
        this.run(new String[] {"help"}).assertOk("");

        this.shell.assertPrintCount(1)
            .assertPrint(0, Resource.getContent("help.txt"));
    }

    @Test
    public void testHelpOptionShort() throws Exception {
        this.run(new String[] {"-h"}).assertOk("");

        this.shell.assertPrintCount(1)
            .assertPrint(0, Resource.getContent("help.txt"));
    }

    @Test
    public void testHelpOptionLong() throws Exception {
        this.run(new String[] {"--help"}).assertOk("");

        this.shell.assertPrintCount(1)
            .assertPrint(0, Resource.getContent("help.txt"));
    }
}