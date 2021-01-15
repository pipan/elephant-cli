package com.pipan.elephant.integration;

import com.pipan.elephant.Resource;
import com.pipan.filesystem.DirectoryMock;
import com.pipan.filesystem.SymbolicLinkMock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RollbackIntegrationTest extends IntegrationTestCase {
    @Test
    public void testNotInitilizedDirectory() throws Exception {
        this.filesystemSeeder.empty(this.filesystem);
        this.run(new String[] {"rollback"}).assertFailed("No rollback version available");

        this.shell.assertPrintErrorCount(1);
        this.shell.assertPrintError(0, "No rollback version available");
    }

    @Test
    public void testInvalidJson() throws Exception {
        this.filesystemSeeder.upgrade(this.filesystem, 2);
        this.filesystem.getFile("elephant.json").write(Resource.getContent("template/elephant_invalid_json.json"));

        this.run(new String[] {"rollback"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Rollback successful");

        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("production_link")).assertTarget("releases/1");
        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("stage_link")).assertTarget("releases/2");
    }

    @Test
    public void testInvalidSource() throws Exception {
        this.filesystemSeeder.upgrade(this.filesystem, 2);
        this.filesystem.getFile("elephant.json").write(Resource.getContent("template/elephant_invalid_source.json"));

        this.run(new String[] {"rollback"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Rollback successful");

        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("production_link")).assertTarget("releases/1");
        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("stage_link")).assertTarget("releases/2");
    }

    @Test
    public void testSingleVersion() throws Exception {
        this.filesystemSeeder.upgrade(this.filesystem);

        this.run(new String[] {"rollback"}).assertFailed("No rollback version available");

        this.shell.assertPrintErrorCount(1);
        this.shell.assertPrintError(0, "No rollback version available");
    }

    @Test
    public void testFourVersions() throws Exception {
        this.filesystemSeeder.upgrade(this.filesystem, 4);

        this.run(new String[] {"rollback"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Rollback successful");

        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("production_link")).assertTarget("releases/3");
        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("stage_link")).assertTarget("releases/4");
        this.shell.assertExecuted("sudo systemctl reload php-fpm");
    }

    @Test
    public void testStageAhead() throws Exception {
        this.filesystemSeeder.upgrade(this.filesystem, 4);
        this.filesystemSeeder.addStage(this.filesystem, "5");

        this.run(new String[] {"rollback"}).assertOk("");

        this.shell.assertPrintCount(1);
        this.shell.assertPrint(0, "Rollback successful");

        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("production_link")).assertTarget("releases/3");
        ((SymbolicLinkMock) this.filesystem.getSymbolicLink("stage_link")).assertTarget("releases/5");

        ((DirectoryMock) this.filesystem.getDirectory("releases")).assertChildMissing("4");
    }
}