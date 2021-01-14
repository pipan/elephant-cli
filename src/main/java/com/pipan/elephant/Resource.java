package com.pipan.elephant;

import java.io.File;
import java.nio.file.Files;

public class Resource {
    public static String getContent(String name) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
 
        File file = new File(classLoader.getResource(name).getFile());
        
        if (!file.exists()) {
            throw new Exception("Resource was not found: " + name);
        }
        
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded);
    }
}