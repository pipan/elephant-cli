package com.pipan.elephant.source.githubrelease;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.pipan.elephant.archive.ArchiveService;
import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.github.GithubApi;
import com.pipan.elephant.output.ConsoleOutput;
import com.pipan.elephant.service.ComposerService;
import com.pipan.elephant.source.Upgrader;
import com.pipan.filesystem.Directory;

import org.json.JSONArray;
import org.json.JSONException;

public class GithubReleaseUpgrader implements Upgrader {
    private ConsoleOutput output;
    private GithubApi api;
    private ArchiveService archiveService;

    public GithubReleaseUpgrader(ConsoleOutput output, GithubApi api, ArchiveService archiveService) {
        this.output = output;
        this.api = api;
        this.archiveService = archiveService;
    }

    public boolean upgrade(Directory dir, ElephantConfig config) throws Exception {    
        GithubReleaseSourceConfig gitConfig = new GithubReleaseSourceConfig(config.getSource());
        this.output.write("[...] Github release: Checking available versions");

        JSONArray json = this.api.releases(gitConfig.getRepository());
        if (json.length() == 0) {
            this.output.rewrite("[ - ] Github release: No release found for repository " + gitConfig.getRepository());
            return false;    
        }
        String version = json.getJSONObject(0).getString("tag_name");
        this.output.rewrite("[...] Github release: Downloading assets for version <blue>" + version + "</blue>");
        InputStream assetFile = new URL("https://github.com/" + gitConfig.getRepository() + "/releases/download/" + version + "/" + gitConfig.getAsset()).openStream();

        this.output.rewrite("[...] Github release: Extracting assets");
        this.archiveService.extract(assetFile, dir.getAbsolutePath());
        this.output.rewrite("[<green> ✔️ </green>] Github release <blue>" + version + "</blue>");
        return true;
    }
}
