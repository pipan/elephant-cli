package com.pipan.elephant.integration;

import com.pipan.elephant.Resource;
import com.pipan.filesystem.DirectoryMock;
import com.pipan.filesystem.SymbolicLinkMock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UpgradeIntegrationTest extends IntegrationTestCase {
    @Test
    public void testNotInitilizedDirectory() throws Exception {
        this.filesystemSeeder.empty(this.filesystem);
        this.run(new String[] {"upgrade"}).assertFailed("Elephant file is missing");

        this.shell.assertPrintErrorCount(1);
        this.shell.assertPrintError(0, "Elephant file is missing");
    }

    @Test
    public void testInitializedElephantInvalidJson() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);
        this.filesystem.getFile("elephant.json").write(Resource.getContent("template/elephant_invalid_json.json"));

        this.run(new String[] {"upgrade"}).assertFailed("Elephant file is invalid");

        this.shell.assertPrintErrorCount(1);
        this.shell.assertPrintError(0, "Elephant file is invalid");
    }

    @Test
    public void testInitializedElephantInvalidSource() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);
        this.filesystem.getFile("elephant.json").write(Resource.getContent("template/elephant_invalid_source.json"));

        this.run(new String[] {"upgrade"}).assertFailed("Invalid source: unknown source type unknown");

        this.shell.assertPrintErrorCount(1);
        this.shell.assertPrintError(0, "Invalid source: unknown source type unknown");
    }

    @Test
    public void testUpgrade() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);

        this.run(new String[] {"upgrade"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Upgrade successful");

        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("stage_link")).assertTarget("releases/1");
        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("production_link")).assertTarget("releases/1");
        this.shell.assertExecuted("sudo systemctl reload php-fpm");
    }

    @Test
    public void testUpgradeStageAhead() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);
        this.filesystemSeeder.addStage(this.filesystem, "2");

        this.run(new String[] {"upgrade"}).assertOk("");

        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("stage_link")).assertTarget("releases/2");
        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("production_link")).assertTarget("releases/2");
    }

    @Test
    public void testRemoveUnusedUpgrades() throws Exception {
        this.filesystemSeeder.upgrade(this.filesystem);
        this.filesystemSeeder.addStage(this.filesystem, "2");
        this.filesystemSeeder.addStage(this.filesystem, "3");
        this.filesystemSeeder.addStage(this.filesystem, "4");
        this.filesystemSeeder.addStage(this.filesystem, "5");
        this.filesystemSeeder.addStage(this.filesystem, "6");
        this.filesystemSeeder.setProduction(this.filesystem, "6");

        this.run(new String[] {"upgrade"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Upgrade successful");

        ((DirectoryMock) this.filesystem.getDirectory("releases")).assertChildMissing("1");
    }

    // todo test finnish directory initialization
}