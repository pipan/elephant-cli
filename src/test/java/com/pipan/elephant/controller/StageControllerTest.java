package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.elephant.DirectoryMock;
import com.pipan.elephant.FileMock;
import com.pipan.elephant.command.AssertableCommandResult;
import com.pipan.filesystem.ReadException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StageControllerTest extends ControllerTemplate {
    private AssertableCommandResult executeController() throws Exception {
        Controller controller = new StageController(this.releases, this.upgraderRepository);
        CommandResult result = controller.execute(new Command("stage"));
        return new AssertableCommandResult(result);
    }

    @Test
    public void testConfigFileNotFound() throws Exception {
        this.executeController().assertFailed("Config file not found");
    }

    @Test
    public void testConfigInvalidJson() throws Exception {
        Assertions.assertThrows(ReadException.class, () -> {
            ((FileMock) this.mockFilesystem.getFile("elephant.json"))
                .withReadException(new ReadException("Mock read exception"))
                .write("{\"source\":\"test-source}");
            this.executeController();
        });
    }

    @Test
    public void testUpgraderNotFound() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source-not\"}");
        this.executeController().assertFailed("Source unknown");
    }

    @Test
    public void testCreateNewStageCallUpgrader() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");

        this.executeController();

        Assertions.assertNotNull(this.upgrader.getDir());
        Assertions.assertEquals("releases/1", this.upgrader.getDir().getAbsolutePath());
    }

    @Test
    public void testCreateNewStageFirstStage() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");

        this.executeController().assertOk();

        Assertions.assertEquals("releases/1", this.mockFilesystem.getSymbolicLink("stage_link").getTargetString());
    }

    public void testCreateNewStageSecondStage() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/1");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/1");

        this.executeController().assertOk();

        Assertions.assertEquals("releases/2", this.mockFilesystem.getSymbolicLink("stage_link").getTargetString());
    }

    @Test
    public void testCreateNewStageStageAheadOne() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/2");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/1");

        this.executeController().assertOk();

        Assertions.assertEquals("releases/3", this.mockFilesystem.getSymbolicLink("stage_link").getTargetString());
    }

    @Test
    public void testCreateNewStageStageAheadMultiple() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/20");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/10");

        this.executeController().assertOk();

        Assertions.assertEquals("releases/21", this.mockFilesystem.getSymbolicLink("stage_link").getTargetString());
    }

    @Test
    public void testCreateNewStageStageBehind() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/1");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/2");

        this.executeController().assertFailed("Cannot create new stage: stage is behind production");
    }

    @Test
    public void testCreateNewStageWithEmptyConfig() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/2");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/1");

        this.executeController();

        Assertions.assertNotNull(this.upgrader.getConfig());
        Assertions.assertTrue(this.upgrader.getConfig().isEmpty());
    }

    @Test
    public void testCreateNewStageWithConfig() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\",\"test-source\":{\"key\":\"value\"}}");

        this.executeController();

        Assertions.assertNotNull(this.upgrader.getConfig());
        Assertions.assertEquals("value", this.upgrader.getConfig().getString("key"));
    }

    @Test
    public void testRemoveUnusedStages() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/4");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/2");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1")
            .withChild("2")
            .withChild("3")
            .withChild("4");
        
        this.executeController();

        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("1").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("2").exists());
        Assertions.assertFalse(this.mockFilesystem.getDirectory("releases").enterDir("3").exists());
        Assertions.assertFalse(this.mockFilesystem.getDirectory("releases").enterDir("4").exists());
    }

    // TODO test upgrader cannot create dir
}