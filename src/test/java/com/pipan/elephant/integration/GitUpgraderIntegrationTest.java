package com.pipan.elephant.integration;

import com.pipan.elephant.Resource;
import com.pipan.filesystem.DirectoryMock;
import com.pipan.filesystem.SymbolicLinkMock;
import com.pipan.elephant.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GitUpgraderIntegrationTest extends IntegrationTestCase {
    @Test
    public void testGitClone() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);
        this.filesystem.getFile("elephant.json").write(Resource.getContent("template/git/elephant.json"));

        this.run(new String[] {"stage"}).assertOk("");

        this.shell.assertExecuted("git clone -q https://git.url.test/repo releases/1");
    }

    @Test
    public void testGitCloneWithComposer() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);
        this.filesystem.getFile("elephant.json").write(Resource.getContent("template/git/elephant_with_composer.json"));

        this.run(new String[] {"stage"}).assertOk("");

        this.shell.assertExecuted("git clone -q https://git.url.test/repo releases/1");
        this.shell.assertExecuted("composer -q install --no-dev -o -d releases/1");
    }

    @Test
    public void testGitCloneBranch() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);
        this.filesystem.getFile("elephant.json").write(Resource.getContent("template/git/elephant_branch.json"));

        this.run(new String[] {"stage"}).assertOk("");

        this.shell.assertExecuted("git clone -q --single-branch -b develop https://git.url.test/repo releases/1");
    }
}