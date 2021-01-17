package com.pipan.elephant.integration;

import com.pipan.elephant.Resource;
import com.pipan.filesystem.DirectoryMock;
import com.pipan.filesystem.FileMock;
import com.pipan.filesystem.SymbolicLinkMock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StageIntegrationTest extends IntegrationTestCase {
    @Test
    public void testNotInitilizedDirectory() throws Exception {
        this.filesystemSeeder.empty(this.filesystem);
        this.run(new String[] {"stage"}).assertFailed("Elephant file is missing");

        this.shell.assertPrintErrorCount(1);
        this.shell.assertPrintError(0, "Elephant file is missing");
    }

    @Test
    public void testInitializedElephantInvalidJson() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);
        this.filesystem.getFile("elephant.json").write(Resource.getContent("template/elephant_invalid_json.json"));

        this.run(new String[] {"stage"}).assertFailed("Elephant file is invalid");

        this.shell.assertPrintErrorCount(1);
        this.shell.assertPrintError(0, "Elephant file is invalid");
    }

    @Test
    public void testInitializedElephantInvalidSource() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);
        this.filesystem.getFile("elephant.json").write(Resource.getContent("template/elephant_invalid_source.json"));

        this.run(new String[] {"stage"}).assertFailed("Invalid source: unknown source type unknown");

        this.shell.assertPrintErrorCount(1);
        this.shell.assertPrintError(0, "Invalid source: unknown source type unknown");
    }

    @Test
    public void testStage() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);

        this.run(new String[] {"stage"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Stage successful");

        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("stage_link")).assertTarget("releases/1");
    }

    @Test
    public void testStageHooks() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);
        this.filesystem.withFile("stage.before", "");
        this.filesystem.withFile("stage.after", "");

        this.run(new String[] {"stage"}).assertOk("");

        this.shell.assertExecuted("/stage.before ");
        this.shell.assertExecuted("/stage.after ");
    }

    @Test
    public void testRemoveUnusedStages() throws Exception {
        this.filesystemSeeder.upgrade(this.filesystem);
        this.filesystemSeeder.addStage(this.filesystem, "2");

        this.run(new String[] {"stage"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Stage successful");

        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("stage_link")).assertTarget("releases/3");
        ((DirectoryMock) this.filesystem.getDirectory("releases")).assertChildMissing("2");
    }

    @Test
    public void testStageBehind() throws Exception {
        this.filesystemSeeder.upgrade(this.filesystem, 2);
        this.filesystemSeeder.setStage(this.filesystem, "1");

        this.run(new String[] {"stage"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Stage successful");

        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("stage_link")).assertTarget("releases/3");
    }
}