package com.pipan.elephant.integration;

import org.junit.jupiter.api.Test;

public class CommandIntegrationTest extends IntegrationTestCase {
    @Test
    public void testUnknownCommand() throws Exception {
        this.run(new String[] {"unknown"}).assertFailed("Command not found: unknown");
    }
}