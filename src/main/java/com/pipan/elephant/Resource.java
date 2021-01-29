package com.pipan.elephant;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Resource {
    public static String getContent(String name) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
 
        String content = "";
        InputStream inputStream = classLoader.getResourceAsStream(name);
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        for (String line; (line = reader.readLine()) != null;) {
            if (!content.isEmpty()) {
                content += System.lineSeparator();
            }
            content += line;
        }
        
        return content;
    }
}