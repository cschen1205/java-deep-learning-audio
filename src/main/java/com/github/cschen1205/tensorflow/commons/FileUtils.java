package com.github.cschen1205.tensorflow.commons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<String> getAudioFilePaths(String folderPath, String extension) {
        List<String> result = new ArrayList<>();
        File dir = new File(folderPath);
        System.out.println(dir.getAbsolutePath());
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                String file_path = f.getAbsolutePath();
                if (file_path.endsWith(extension)) {
                    result.add(file_path);
                }
            }
        }

        return result;
    }

    public static File[] getAudioFiles(String folderPath, String extension) {
        List<File> result = new ArrayList<>();
        File dir = new File(folderPath);
        System.out.println(dir.getAbsolutePath());
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                String file_path = f.getAbsolutePath();
                if (file_path.endsWith(extension)) {
                    result.add(f);
                }
            }
        }

        File[] files = new File[result.size()];
        for(int i=0; i < files.length;++i) {
            files[i] = result.get(i);
        }
        return files;
    }

}
