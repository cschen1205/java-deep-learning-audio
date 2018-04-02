package com.github.cschen1205.tensorflow.commons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<String> getAudioFilePaths() {
        List<String> result = new ArrayList<>();
        File dir = new File("music_samples");
        System.out.println(dir.getAbsolutePath());
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                String file_path = f.getAbsolutePath();
                if (file_path.endsWith("au")) {
                    result.add(file_path);
                }
            }
        }

        return result;
    }

    public static File[] getAudioFiles() {
        List<File> result = new ArrayList<>();
        File dir = new File("music_samples");
        System.out.println(dir.getAbsolutePath());
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                String file_path = f.getAbsolutePath();
                if (file_path.endsWith(".au")) {
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

    public static File[] getImageFiles() {
        List<File> result = new ArrayList<>();
        File dir = new File("image_samples");
        System.out.println(dir.getAbsolutePath());

        getImageFiles(dir, result);

        File[] files = new File[result.size()];
        for(int i=0; i < files.length;++i) {
            files[i] = result.get(i);
        }
        return files;
    }

    private static void getImageFiles(File dir, List<File> result) {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                if(f.isDirectory()){
                    getImageFiles(f, result);
                } else {
                    String file_path = f.getAbsolutePath();
                    if (file_path.endsWith(".png")) {
                        result.add(f);
                    }
                }
            }
        }
    }

    public static List<String> getImageFilePaths() {
        List<String> result = new ArrayList<>();
        File[] files = getImageFiles();
        for(File f : files) {
            result.add(f.getAbsolutePath());
        }
        return result;
    }
}
