package com.pipan.elephant.controller;

import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.controller.Controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InitControllerTest extends ControllerTemplate {
    @Test
    public void testConfigFileExists() throws Exception {
        this.mockFilesystem.getFile("elephant.json").write("{}");

        Controller controller = new InitController(this.workingDirectory);
        CommandResult result = controller.execute(new Command("init"));
        Assertions.assertEquals(1, result.getCode());
        Assertions.assertEquals("Config file exists", result.getMessage());
    }

    @Test
    public void testConfigFileDoesNotExist() throws Exception {
        Controller controller = new InitController(this.workingDirectory);
        CommandResult result = controller.execute(new Command("init"));
        Assertions.assertEquals(0, result.getCode());
        Assertions.assertEquals("Done", result.getMessage());

        String configContent = this.workingDirectory.getConfigFile().read();
        Assertions.assertEquals("{\"git\":{\"composer\":true,\"url\":\"\"},\"source\":\"git\",\"history_limit\":5}", configContent);
        Assertions.assertTrue(this.mockFilesystem.getDirectory("releases").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("public").exists());
        Assertions.assertTrue(this.mockFilesystem.getDirectory("public").enterDir(".well-known").exists());
    }
}