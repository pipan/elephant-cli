package com.pipan.elephant.integration;

import com.pipan.filesystem.Filesystem;
import com.pipan.filesystem.FilesystemMock;
import com.pipan.filesystem.FileMock;
import com.pipan.filesystem.DirectoryMock;
import com.pipan.filesystem.SymbolicLinkMock;
import com.pipan.elephant.Resource;

public class FilesystemSeeder {
    public void empty(FilesystemMock filesystem) throws Exception {
        filesystem.withFile("elephant.json");
        filesystem.withDir("releases", new DirectoryMock("releases"));
        filesystem.withSymbolicLink("production_link", new SymbolicLinkMock());
        filesystem.withSymbolicLink("stage_link", new SymbolicLinkMock());
    }

    public void initializeElephant(FilesystemMock filesystem) throws Exception {
        this.empty(filesystem);
        filesystem.getFile("elephant.json").write(Resource.getContent("template/elephant.json"));
    }

    public void initializeStructure(FilesystemMock filesystem) throws Exception {
        this.initializeElephant(filesystem);
        filesystem.getDirectory("releases").make();
    }

    public void stage(FilesystemMock filesystem) throws Exception {
        this.stage(filesystem, 1);
    }

    public void stage(FilesystemMock filesystem, Integer versionCount) throws Exception {
        this.initializeStructure(filesystem);
        for (Integer i = 1; i <= versionCount; i++) {
            this.addStage(filesystem, i.toString());
        }
    }

    public void upgrade(FilesystemMock filesystem) throws Exception {
        this.upgrade(filesystem, 1);
    }

    public void upgrade(FilesystemMock filesystem, Integer versionCount) throws Exception {
        this.stage(filesystem, versionCount);
        this.setProduction(filesystem, versionCount.toString());
    }

    public void rollback(FilesystemMock filesystem) throws Exception {
        this.upgrade(filesystem);
        this.addStage(filesystem, "2");
    }

    public void addStage(FilesystemMock filesystem, String dir) throws Exception {
        ((DirectoryMock) filesystem.getDirectory("releases")).withChild(dir);
        this.setStage(filesystem, dir);
    }

    public void setProduction(FilesystemMock filesystem, String dir) throws Exception {
        filesystem.getSymbolicLink("production_link").setTarget("releases/" + dir);
    }

    public void setStage(FilesystemMock filesystem, String dir) throws Exception {
        filesystem.getSymbolicLink("stage_link").setTarget("releases/" + dir);
    }
}