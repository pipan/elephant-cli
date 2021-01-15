package com.pipan.elephant.integration;

import com.pipan.elephant.AppBootstrap;
import com.pipan.cli.command.Command;
import com.pipan.cli.command.CommandResult;
import com.pipan.cli.Kernel;
import com.pipan.elephant.shell.ShellMock;
import com.pipan.filesystem.FilesystemMockFactory;
import com.pipan.filesystem.FilesystemFactory;
import com.pipan.filesystem.FilesystemMock;
import com.pipan.filesystem.Filesystem;
import com.pipan.elephant.command.AssertableCommandResult;

import org.junit.jupiter.api.BeforeEach;

public class IntegrationTestCase {
    protected Kernel kernel;
    protected FilesystemMock filesystem;
    protected FilesystemSeeder filesystemSeeder = new FilesystemSeeder();
    protected ShellMock shell;

    @BeforeEach
    public void setUp() {
        this.filesystem = new FilesystemMock();
        FilesystemFactory filesystemFactory = new FilesystemMockFactory(this.filesystem);
        this.shell = new ShellMock();

        this.kernel = new Kernel(new AppBootstrap(filesystemFactory, this.shell));
    }

    protected AssertableCommandResult run(String[] args) throws Exception {
        CommandResult result = this.kernel.run(Command.fromArgs(args));
        return new AssertableCommandResult(result);
    }
}