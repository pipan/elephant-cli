# Elephant

[![Build Status](https://travis-ci.com/pipan/elephant-cli.svg?branch=master)](https://travis-ci.com/pipan/elephant-cli)

CLI tool for deploying PHP applications

## Installation

### Linux

```
sudo add-apt-repository ppa:gipn/elephant
sudo apt-get update
```

> [ppa](https://launchpad.net/~gipn/+archive/ubuntu/elephant)

## Usage

### Initialize

Every project has the same initialization process. Only difference is in the configuration. Start by creating directory for you project.

```
mkdir /some/app/directory
cd /some/app/directory
elephant init
```

`elephant init` will create a configuration file called `elephant.json` and it will also create some internal directory structure. Open `elephant.json` and change this file according to the source you want to use. The list of available sources:

* [Git](#git)
* [Composer Project](#composer-project)
* [Github Release](#github-release)

The last thing will be to make a first deploy. For that follow instructions in [upgrade](#upgrade) section.

### Upgrade

The point of upgrade command is to change symbolic link `production_link` to the newest release in `releases` directory.

```
cd /some/app/directory
elephant upgrade
```

This command will call [upgrade.before](#upgrade-before) and [upgrade.after](#upgrade-after) hooks.

> This action will internaly run stage command if the current `production_link` is linked to the latest release in `releases` directory

### Stage

Stage command is responsible for downloading next version to releases directory and changing `stage_link` symbolic link to this new directory.

```
cd /some/app/directory
elephant stage
```

This command will call [stage.before](#stage-before) and [stage.after](#stage-after) hooks.

### Rollback

Rollback command will change `production_link` symbolic link to previous version in `releases` directory, if any previous version exists.

```
cd /some/app/directory
elephant rollback
```

This command will call [rollback.before](#rollback-before) and [rollback.after](#rollback-after) hooks.

### Options

**Verbose output**

Every command can run with `--verbose` swithc, which will output detailed information about current processes. Use it to debug unexpected behavior.

**Run Elephant from Different Directory**

You will usualyy run elephant command inside the project you want to deploy. However, you may wish to run elephant command from different directory. To set the working directory use `-d=/path/to/project` swich with absolute path to project.

## Sources

### Git

Deploy project from git repository. Example of source configuration

```json
{
    "source":{
        "type":"git",
        "url":"https://git.url.com/user/repo",
        "composer":"optional boolean",
        "branch":"optional default master"
    }
}
```

This source will always clone repository to the new release and checkout specified branch. If you don't specify any branch, then default branch will be used. You can also set `composer` value to `true` if you want to also install composer dependencies. Theese dependencies are production optimized.

### Composer Project

Deploy project from composer project.

```json
{
    "source":{
        "type":"composer-project",
        "package":"vendor/name"
    }
}
```

This source will download the newest version of project in composer. Composer is executed with cache off, so the composer should download the newest version. Bare in mind, that sometimes it takes a few minutes to propagate new version of a project from composer server to your machine.

### Github Release

Deploy project from github release.

```json
{
    "source":{
        "type":"github-release",
        "repository":"owner/repository-name",
        "asset":"filename.zip"
    }
}
```

This source will check the latest release version via github API and download asset specified in the source config. Asset has to be only one `.zip` file. This will be unziped into releases directory. This source method can be very usefull for deploying frontend bundles (HTML, CSS and JS).

## Configuration

List of general configuration values, that can be applied for any source

* [receipt](#receipts)
* **history_limit** - integer, default 5. The maximum number of available rollbacks at any time. This number is checked after every upgrade and if there is more releases then this number in `releases` direcotory, the oldest releease are removed automatically.

## Hooks

Your application may be a little bit more complex and need some more attantion after upgrade, stage or rollback. We allow you to add you own scripts that will be automatically executed before and after a specific action. Convention is to create a file in project directory and name it `<command>.<type>` (ex. `stage.after`)

### Stage Before

file: `stage.before`

This hook will be executed before new stage is downloaded to `releases` directory.

### Stage After

file: `stage.after`

This hook will be executed after new stage has been downloaded to `releases` directory and linked to `stage_link`.

### Upgrade Before

file: `upgrade.before`

This hook will be executed before new upgrade is switched. If new release has to be downloaded this hook will be executed after `stage.after`.

### Upgrade After

file: `upgrade.after`

This hook will be executed after `production_link` has been linked to new release.

### Rollback Before

file: `rollback.before`

This hook will be executed before `procution_link` has been set to older release.

### Rollback After

file: `rollback.after`

This hook will be executed afer `procution_link` has been set to older release.

## Receipts

### Laravel