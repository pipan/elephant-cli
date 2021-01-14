package com.pipan.elephant.integration;

import com.pipan.elephant.Resource;
import com.pipan.filesystem.DirectoryMock;
import com.pipan.filesystem.SymbolicLinkMock;
import com.pipan.elephant.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ComposerProjectIntegrationTest extends IntegrationTestCase {
    @Test
    public void testComposerProject() throws Exception {
        this.filesystemSeeder.initializeElephant(this.filesystem);
        this.filesystem.getFile("elephant.json").write(Resource.getContent("template/composerproject/elephant.json"));

        this.run(new String[] {"stage"}).assertOk("");

        this.shell.assertExecuted("composer create-project --no-cache --no-dev --prefer-dist package/name releases/1");
    }
}