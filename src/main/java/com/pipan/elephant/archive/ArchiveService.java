package com.pipan.elephant.archive;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ArchiveService {

    public ArchiveService() {}

    public void extract(InputStream archive, String destination) throws Exception {
        ZipInputStream zip = new ZipInputStream(archive);

        new File(destination).mkdir();

        while (true) {
            ZipEntry entry = zip.getNextEntry();
            if (entry == null) {
                break;
            }
            String currentEntry = entry.getName();

            File destFile = new File(destination, currentEntry);
            File destinationParent = destFile.getParentFile();

            destinationParent.mkdirs();

            if (entry.isDirectory()) {
                continue;
            }

            Files.copy(zip, destFile.toPath());
        }
    }
}
