package com.github.cschen1205.tensorflow;

import com.github.cschen1205.tensorflow.commons.FileUtils;
import com.github.cschen1205.tensorflow.search.models.AudioSearchEntry;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class DeepAudioDemo {
    public static void main(String[] args){
        predictMusicGenres();
        audioSearchEngine();
    }

    private static void predictMusicGenres() {
        DeepAudio classifier = new DeepAudioTensorflow();

        String folderStoringMusicFiles = "music_samples";
        List<String> paths = FileUtils.getAudioFilePaths(folderStoringMusicFiles, ".au");

        Collections.shuffle(paths);

        for (String path : paths) {
            System.out.println("Predicting " + path + " ...");
            File f = new File(path);
            String label = classifier.predictMusicGenres(f);

            System.out.println("Predicted: " + label);
        }
    }

    private static void audioSearchEngine() {
        DeepAudio searchEngine = new DeepAudioTensorflow();
        if(!searchEngine.loadMusicIndexDbIfExists()) {
            String folderStoringMusicFiles = "music_samples";
            searchEngine.indexMusicFiles(FileUtils.getAudioFiles(folderStoringMusicFiles, ".au"));
            searchEngine.saveMusicIndexDb();
        }

        int pageIndex = 0;
        int pageSize = 20;
        boolean skipPerfectMatch = true;
        File sample_file = new File("mp3_samples/example.mp3");
        System.out.println("querying similar music to " + sample_file.getName());
        List<AudioSearchEntry> result = searchEngine.query(sample_file, pageIndex, pageSize, skipPerfectMatch);
        for(int i=0; i < result.size(); ++i){
            System.out.println("# " + i + ": " + result.get(i).getPath() + " (distSq: " + result.get(i).getDistance() + ")");
        }
    }
}
