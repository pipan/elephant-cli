package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;
import com.pipan.elephant.DirectoryMock;
import com.pipan.elephant.command.AssertableCommandResult;

import org.junit.jupiter.api.Test;

public class StatusControllerTest extends ControllerTemplate {
    private AssertableCommandResult executeController() throws Exception {
        Controller controller = new StatusController(this.releases);
        CommandResult result = controller.execute(new Command("status"));
        return new AssertableCommandResult(result);
    }

    @Test
    public void testNoReleaseDirectory() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");

        this.executeController().assertFailed("Releases directory does not exists");
        
    }

    @Test
    public void testEmptyReleaseDirectory() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        this.mockFilesystem.getDirectory("releases").make();

        this.executeController().assertFailed("Empty releases directory");
    }

    @Test
    public void testOneDirectory() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        this.mockFilesystem.getDirectory("releases").make();
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1");

        this.executeController().assertOk("1/n");
    }

    @Test
    public void testMultipleDirectories() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        this.mockFilesystem.getDirectory("releases").make();
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1")
            .withChild("2")
            .withChild("test");

        this.executeController().assertOk("1/n2/ntest/n");
    }

    @Test
    public void testProductionAndStageSameDirectory() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/1");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/1");
        this.mockFilesystem.getDirectory("releases").make();
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1")
            .withChild("2")
            .withChild("3");

        this.executeController().assertOk("1 - production, stage/n2/n3/n");
    }

    @Test
    public void testProductionAndStageDifferentDirectory() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");
        this.mockFilesystem.getSymbolicLink("production_link").setTarget("releases/2");
        this.mockFilesystem.getSymbolicLink("stage_link").setTarget("releases/3");
        this.mockFilesystem.getDirectory("releases").make();
        ((DirectoryMock) this.mockFilesystem.getDirectory("releases"))
            .withChild("1")
            .withChild("2")
            .withChild("3");

        this.executeController().assertOk("1/n2 - production/n3 - stage/n");
    }
}
