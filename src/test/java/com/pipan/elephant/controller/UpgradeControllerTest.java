package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.elephant.DirectoryMock;
import com.pipan.elephant.command.AssertableCommandResult;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UpgradeControllerTest extends ControllerTemplate {
    private AssertableCommandResult executeController() throws Exception {
        Controller controller = new UpgradeController(this.releases, this.upgraderRepository, this.apache);
        CommandResult result = controller.execute(new Command("upgrade"));
        return new AssertableCommandResult(result);
    }

    @Test
    public void testProductionLinkNull() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");

        this.executeController().assertOk();

        Assertions.assertEquals("releases/1", this.mockFilesystem.getSymbolicLink("production_link").getTargetString());
        Assertions.assertEquals("releases/1", this.mockFilesystem.getSymbolicLink("stage_link").getTargetString());
    }

    @Test
    public void testSuccessReloadApache() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");

        this.executeController();

        Assertions.assertEquals(1, this.shellMock.getNumberOfCalls("sudo systemctl reload php-fpm"));
    }

    @Test
    public void testProductionLinkVersionThree() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/3");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/3");

        this.executeController();

        Assertions.assertEquals("releases/4", this.mockFilesystem.getSymbolicLink("production_link").getTargetString());
        Assertions.assertEquals("releases/4", this.mockFilesystem.getSymbolicLink("stage_link").getTargetString());
    }

    @Test
    public void testProductionLinkStageAhead() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/3");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/5");

        this.executeController();

        Assertions.assertEquals("releases/5", this.mockFilesystem.getSymbolicLink("production_link").getTargetString());
        Assertions.assertEquals("releases/5", this.mockFilesystem.getSymbolicLink("stage_link").getTargetString());
    }

    @Test
    public void testLimitRollbackInLimitDefault() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\"}");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1")
            .withChild("2")
            .withChild("3");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/3");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/3");

        this.executeController();

        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("1").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("2").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("3").exists());
    }

    @Test
    public void testLimitRollbackInLimitSet() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\",\"history_limit\":2}");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1")
            .withChild("2");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/2");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/2");

        this.executeController();

        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("1").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("2").exists());
    }

    @Test
    public void testLimitRollbackInLimitSetOneOverLimit() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\",\"history_limit\":2}");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1")
            .withChild("2")
            .withChild("3");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/3");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/3");

        this.executeController();

        Assertions.assertFalse(this.mockFilesystem.getDirectory("releases").enterDir("1").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("2").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("3").exists());
    }

    @Test
    public void testLimitRollbackInLimitSetMultipleOverLimit() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{\"source\":\"test-source\",\"history_limit\":2}");
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1")
            .withChild("2")
            .withChild("3")
            .withChild("4")
            .withChild("5");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/5");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/5");

        this.executeController();

        Assertions.assertFalse(this.mockFilesystem.getDirectory("releases").enterDir("1").exists());
        Assertions.assertFalse(this.mockFilesystem.getDirectory("releases").enterDir("2").exists());
        Assertions.assertFalse(this.mockFilesystem.getDirectory("releases").enterDir("3").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("4").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").enterDir("5").exists());
    }
}
