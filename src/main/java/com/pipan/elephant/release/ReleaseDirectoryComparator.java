package com.pipan.elephant.release;

import java.util.Comparator;

import com.pipan.filesystem.Directory;

public class ReleaseDirectoryComparator implements Comparator<Directory> {

    @Override
    public int compare(Directory dir1, Directory dir2) {
        if (dir2 == null && dir1 == null) {
            return 0;
        }
        if (dir2 == null) {
            return 1;
        }
        if (dir1 == null) {
            return -1;
        }

        int dirInt1 = Integer.parseInt(dir1.getName());
        int dirInt2 = Integer.parseInt(dir2.getName());
        if (dirInt1 == dirInt2) {
            return 0;
        } else if (dirInt1 > dirInt2) {
            return 1;
        }
         return -1;
    }
}
