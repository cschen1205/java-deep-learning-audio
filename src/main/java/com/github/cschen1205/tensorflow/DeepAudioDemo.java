package com.github.cschen1205.tensorflow;

import com.github.cschen1205.tensorflow.commons.UserMusicHistory;
import com.github.cschen1205.tensorflow.commons.FileUtils;
import com.github.cschen1205.tensorflow.search.models.AudioSearchEntry;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class DeepAudioDemo {
    public static void main(String[] args){
        predictMusicGenres();
        musicSearchEngine();
        recommendMusic();
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

    private static UserMusicHistory getUserMusicHistory() {
        UserMusicHistory userHistory = new UserMusicHistory();

        List<String> audioFiles = FileUtils.getAudioFilePaths("music_samples", ".au");
        Collections.shuffle(audioFiles);

        for(int i=0; i < 40; ++i){
            String filePath = audioFiles.get(i);
            userHistory.logAudio(filePath);
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return userHistory;
    }

    private static void recommendMusic() {
        UserMusicHistory userHistory = getUserMusicHistory();

        DeepAudio recommender = new DeepAudioTensorflow();
        if(!recommender.loadMusicIndexDbIfExists()) {
            recommender.indexMusicFiles(FileUtils.getAudioFiles("music_samples", ".au"));
            recommender.saveMusicIndexDb();
        }

        System.out.println(userHistory.head(10));

        int k = 10;
        List<AudioSearchEntry> result = recommender.recommends(userHistory.getHistory(), k);

        for(int i=0; i < result.size(); ++i){
            AudioSearchEntry entry = result.get(i);
            System.out.println("Search Result #" + (i+1) + ": " + entry.getPath());
        }
    }

    private static void musicSearchEngine() {
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
