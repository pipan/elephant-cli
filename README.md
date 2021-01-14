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

```
cd /some/app/directory
elephant init
```

This command creates directory structure for application to live in.

* releases - direcotry where new versions will be deployed
* public - directory where your publically available scripts will be. They can be linked via `production_link` or `stage_link`
* elephant.json - configuration file for application deployment process

### Configuration

There are generale values for configuration and then there are specific configuration parts, depending what source you choose.

**General**

```json
{
    "source":"composer-project | git",
    "history_limit":"int",
    "public":"string",
    "well-known":"boolean",
    "receipt":"laravel"
}
```

**composer-project**

```json
{
    "source":"composer-project",
    "history_limit": "",
    "composer-project":{
        "package":"string"
    }
}
```

**git**

```json
{
    "source":"git",
    "history_limit": "",
    "git":{
        "url":"string",
        "composer":"boolean"
    }
}
```

