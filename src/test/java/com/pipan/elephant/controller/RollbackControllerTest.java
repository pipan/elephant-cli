package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.elephant.DirectoryMock;
import com.pipan.elephant.command.AssertableCommandResult;
import com.pipan.elephant.progress.ConsoleProgress;
import com.pipan.elephant.progress.Progress;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RollbackControllerTest extends ControllerTemplate {
    private AssertableCommandResult executeController() throws Exception {
        Progress progress = new ConsoleProgress();
        Controller controller = new RollbackController(this.releases, this.apache, progress);
        CommandResult result = controller.execute(new Command("rollback"));
        return new AssertableCommandResult(result);
    }

    @Test
    public void testProductionLinkNull() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");

        this.executeController().assertFailed("No rollback version available");
    }

    @Test
    public void testProductionLinkFirst() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/1");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1");

        this.executeController().assertFailed("No rollback version available");

        Assertions.assertEquals("releases/1", this.mockFilesystem.getSymbolicLink("production_link").getTargetString());
    }

    @Test
    public void testProductionLinkOnePreviousVersion() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/2");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1")
            .withChild("2");

        this.executeController().assertOk();

        Assertions.assertEquals("releases/1", this.mockFilesystem.getSymbolicLink("production_link").getTargetString());
    }

    @Test
    public void testProductionRunFpmReload() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/2");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("2")
            .withChild("1");

        this.executeController();

        Assertions.assertEquals(1, this.shellMock.getNumberOfCalls("sudo systemctl reload php-fpm"));
    }

    @Test
    public void testProductionLinkMultiplePreviousVersions() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/10");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1")
            .withChild("2")
            .withChild("4")
            .withChild("10");

        this.executeController().assertOk();

        Assertions.assertEquals("releases/4", this.mockFilesystem.getSymbolicLink("production_link").getTargetString());
    }

    @Test
    public void testProductionLinkMultiplePreviousVersionsAndStage() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/4");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/10");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1")
            .withChild("2")
            .withChild("4")
            .withChild("10");

        this.executeController().assertOk();

        Assertions.assertEquals("releases/2", this.mockFilesystem.getSymbolicLink("production_link").getTargetString());
        Assertions.assertEquals("releases/10", this.mockFilesystem.getSymbolicLink("stage_link").getTargetString());
    }

    @Test
    public void testCleanVersionsBetweenStageAndProductionNothingToClean() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("10")
            .withChild("9")
            .withChild("8");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/10");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/10");

        this.executeController().assertOk();

        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("8").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("9").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("10").exists());
    }

    @Test
    public void testCleanVersionsBetweenStageAndProductionOneToClean() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("10")
            .withChild("9")
            .withChild("8");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/9");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/10");

        this.executeController().assertOk();

        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("8").exists());
        Assertions.assertFalse(this.mockFilesystem.getDirectory("releases").enterDir("9").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("10").exists());
    }

    @Test
    public void testCleanVersionsBetweenStageAndProductionMultipleToClean() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("10")
            .withChild("9")
            .withChild("8")
            .withChild("7")
            .withChild("6");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/8");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/10");

        this.executeController().assertOk();

        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("6").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("7").exists());
        Assertions.assertFalse(this.mockFilesystem.getDirectory("releases").enterDir("8").exists());
        Assertions.assertFalse(this.mockFilesystem.getDirectory("releases").enterDir("9").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("10").exists());
    }
}
