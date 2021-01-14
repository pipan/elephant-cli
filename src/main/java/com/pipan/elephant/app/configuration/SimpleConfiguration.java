package com.pipan.elephant.app.configuration;

public class SimpleConfiguration implements Configuration {
    private boolean wellKnown = true;
    private String publicDir = "";

    public void setWellKnown(boolean value) {
        this.wellKnown = value;
    }

    public void withPublicDirectory(String path) {
        this.publicDir = path;
    }

    public boolean hasWellKnown() {
        return this.wellKnown;
    }

    public String getPublicDirectory() {
        return this.publicDir;
    }
}
