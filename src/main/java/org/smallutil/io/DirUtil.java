/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smallutil.io;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author MUFEED
 */
public class DirUtil {

    public static boolean hasSubdirectories(File file) {
        return (listDirectories(file) != null);
    }

    public static File[] listDirectories(File file) {
        if (file.isFile()) {
            return null;
        }

        File[] children = file.listFiles();
        if (children == null) {
            return null;
        }

        ArrayList<File> dir = new ArrayList<File>();
        for (int i = 0; i < children.length; i++) {
            if (!children[i].isFile()) {
                dir.add(children[i]);
            }
        }

        if (dir.size() == 0) {
            return null;
        }

        File[] f = new File[dir.size()];
        f = dir.toArray(f);
        return f;
    }

    public static boolean isChild(File file, String child) {
        String[] files = file.list();
        if (files == null) return false;

        for(int i = 0; i < files.length; i++)
            if(files[i].equals(child))
                return true;
        return false;
    }
}
