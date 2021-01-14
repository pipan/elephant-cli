package com.pipan.elephant.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StatusIntegrationTest extends IntegrationTestCase {
    @Test
    public void testNotInitilizedDirectory() throws Exception {
        this.filesystemSeeder.empty(this.filesystem);
        this.run(new String[] {"status"}).assertFailed("Elephant file is missing");

        this.shell.assertPrintErrorCount(1);
        this.shell.assertPrintError(0, "Elephant file is missing");
    }

    @Test
    public void testInitializedElephant() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);

        this.run(new String[] {"status"}).assertOk("No release");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Available releases: no release");
    }

    @Test
    public void testInitializedStructure() throws Exception {
        this.filesystemSeeder.initializeStructure(this.filesystem);

        this.run(new String[] {"status"}).assertOk("No release");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Available releases: no release");
    }

    @Test
    public void testStaged() throws Exception {
    this.filesystemSeeder.stage(this.filesystem);

        this.run(new String[] {"status"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Available releases:\n1        stage");
    }

    @Test
    public void testUpgraded() throws Exception {
        this.filesystemSeeder.upgrade(this.filesystem);

        this.run(new String[] {"status"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Available releases:\n1        production, stage");
    }

    @Test
    public void testRollbacked() throws Exception {
        this.filesystemSeeder.rollback(this.filesystem);

        this.run(new String[] {"status"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Available releases:\n1        production\n2        stage");
    }
}