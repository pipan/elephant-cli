package com.pipan.elephant.source.githubrelease;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.pipan.elephant.archive.ArchiveService;
import com.pipan.elephant.config.ElephantConfig;
import com.pipan.elephant.github.GithubApi;
import com.pipan.elephant.log.Logger;
import com.pipan.elephant.service.ComposerService;
import com.pipan.elephant.service.GitService;
import com.pipan.elephant.source.Upgrader;
import com.pipan.filesystem.Directory;

import org.json.JSONArray;
import org.json.JSONException;

public class GithubReleaseUpgrader implements Upgrader {
    private Logger logger;
    private GithubApi api;
    private ArchiveService archiveService;

    public GithubReleaseUpgrader(Logger logger, GithubApi api, ArchiveService archiveService) {
        this.logger = logger;
        this.api = api;
        this.archiveService = archiveService;
    }

    public boolean upgrade(Directory dir, ElephantConfig config) throws Exception {    
        GithubReleaseSourceConfig gitConfig = new GithubReleaseSourceConfig(config.getSource());
        this.logger.info("Checking available versions");

        JSONArray json = this.api.releases(gitConfig.getRepository());
        if (json.length() == 0) {
            this.logger.info("No release found for repository: " + gitConfig.getRepository());
            return false;    
        }
        String version = json.getJSONObject(0).getString("tag_name");
        this.logger.info("Downloading assets for version: " + version);
        InputStream assetFile = new URL("https://github.com/" + gitConfig.getRepository() + "/releases/download/" + version + "/" + gitConfig.getAsset()).openStream();

        this.logger.info("Extracting assets");
        this.archiveService.extract(assetFile, dir.getAbsolutePath());
        return true;
    }
}
