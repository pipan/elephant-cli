package com.pipan.elephant.source;

import com.pipan.elephant.config.ElephantConfig;
import com.pipan.filesystem.Directory;

public interface Upgrader {
    public boolean upgrade(Directory dir, ElephantConfig config) throws Exception;
}
