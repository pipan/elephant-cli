package com.pipan.elephant.app.configuration;

public class ProxyConfiguration implements Configuration {
    protected Configuration configuration;

    public ProxyConfiguration() {
        this(null);
    }

    public ProxyConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void set(Configuration configuration) {
        this.configuration = configuration;
    }

    public boolean hasWellKnown() {
        return this.configuration.hasWellKnown();
    }

    public String getPublicDirectory() {
        return this.configuration.getPublicDirectory();
    }
}
