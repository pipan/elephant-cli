package com.pipan.elephant.controller;

import com.pipan.elephant.DirectoryMock;
import com.pipan.elephant.FileMock;
import com.pipan.elephant.FilesystemMock;
import com.pipan.elephant.SymbolicLinkMock;
import com.pipan.elephant.UpgraderMock;
import com.pipan.elephant.release.Releases;
import com.pipan.elephant.service.ApacheService;
import com.pipan.elephant.shell.ShellMock;
import com.pipan.elephant.source.UpgraderRepository;
import com.pipan.elephant.workingdir.SimpleWorkingDirectory;
import com.pipan.elephant.workingdir.WorkingDirectory;

import org.junit.jupiter.api.BeforeEach;

public class ControllerTemplate {
    protected FilesystemMock mockFilesystem;
    protected WorkingDirectory workingDirectory;
    protected Releases releases;
    protected UpgraderRepository upgraderRepository;
    protected UpgraderMock upgrader;
    protected ShellMock shellMock;
    protected ApacheService apache;

    @BeforeEach
    public void setUp() {
        this.mockFilesystem = new FilesystemMock();
        this.mockFilesystem.withFile("elephant.json", new FileMock())
            .withDir("releases", new DirectoryMock("releases"))
            .withDir("public", new DirectoryMock("public"))
            .withSymbolicLink("stage_link", new SymbolicLinkMock())
            .withSymbolicLink("production_link", new SymbolicLinkMock());

        this.workingDirectory = SimpleWorkingDirectory.fromConfig(mockFilesystem);
        this.releases = new Releases(this.workingDirectory);
        this.shellMock = new ShellMock();
        this.apache = new ApacheService(this.shellMock);

        this.upgraderRepository = new UpgraderRepository();
        this.upgrader = new UpgraderMock();
        this.upgraderRepository.add("test-source", this.upgrader);
    }
}
