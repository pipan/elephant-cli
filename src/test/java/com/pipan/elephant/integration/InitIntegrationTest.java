package com.pipan.elephant.integration;

import com.pipan.elephant.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InitIntegrationTest extends IntegrationTestCase {
    @Test
    public void testNotInitilizedDirectory() throws Exception {
        this.filesystemSeeder.empty(this.filesystem);
        this.run(new String[] {"init"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Elephant file was created with default values. You should edit this file.");

        this.filesystem.assertFileExists("elephant.json");
        this.filesystem.assertFileContent("elephant.json", Resource.getContent("template/elephant.json"));
    }

    @Test
    public void testInitializedElephant() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);

        this.run(new String[] {"init"}).assertFailed("Elephant file exists");

        this.shell.assertPrintErrorCount(1);
        this.shell.assertPrintError(0, "Elephant file exists");
    }
}