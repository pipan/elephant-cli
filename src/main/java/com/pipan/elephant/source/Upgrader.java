package com.pipan.elephant.source;

import com.pipan.elephant.config.Config;
import com.pipan.filesystem.Directory;

public interface Upgrader {
    public boolean upgrade(Directory dir, Config config) throws Exception;
}
