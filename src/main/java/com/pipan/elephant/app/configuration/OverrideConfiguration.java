package com.pipan.elephant.app.configuration;

public class OverrideConfiguration implements Configuration {
    protected Configuration baseConfiguration;
    private Boolean wellKnown;
    private String publicDir;

    public OverrideConfiguration(Configuration baseConfiguration) {
        this.baseConfiguration = baseConfiguration;
    }

    public void withWellKnown(Boolean value) {
        this.wellKnown = value;
    }

    public void withPublicDirectory(String path) {
        this.publicDir = path;
    }

    public boolean hasWellKnown() {
        if (this.wellKnown == null) {
            return this.baseConfiguration.hasWellKnown();
        }
        return this.wellKnown;
    }

    public String getPublicDirectory() {
        if (this.publicDir == null) {
            return this.baseConfiguration.getPublicDirectory();
        }
        return this.publicDir;
    }
}
