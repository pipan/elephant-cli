package com.pipan.elephant.integration;

import com.pipan.elephant.Resource;
import com.pipan.filesystem.DirectoryMock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InitIntegrationTest extends IntegrationTestCase {
    @Test
    public void testNotInitilizedDirectory() throws Exception {
        this.filesystemSeeder.empty(this.filesystem);
        this.run(new String[] {"init"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Initialization successful");

        this.filesystem.assertFileExists("elephant.json");
        this.filesystem.assertFileContent("elephant.json", Resource.getContent("template/elephant.json"));

        ((DirectoryMock) this.filesystem.getDirectory("releases")).assertExists();
    }

    @Test
    public void testInitializedElephant() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);

        this.run(new String[] {"init"}).assertOk("");

        ((DirectoryMock) this.filesystem.getDirectory("releases")).assertExists();
    }
}