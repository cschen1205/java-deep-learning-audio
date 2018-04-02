# java-deep-learning-audio

Audio Deep Learning Project in Java

# Predict Music Genres

The [sample codes](src/main/java/com/github/cschen1205/tensorflow/DeepAudioDemo.java) shows how to 
[DeepAudio](src/main/java/com/github/cschen1205/tensorflow/DeepAudioTensorflow.java) to predict the genres of an
music file:

```java
import com.github.cschen1205.tensorflow.commons.FileUtils;
import com.github.cschen1205.tensorflow.search.models.AudioSearchEntry;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class MusicGenrePredictionDemo {
    public static void main(String[] args){
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
}

```

# Music Search

The [sample codes](src/main/java/com/github/cschen1205/tensorflow/DeepAudioDemo.java) shows how to 
[DeepAudio](src/main/java/com/github/cschen1205/tensorflow/DeepAudioTensorflow.java) to search for similar musics stored
in your local folder using search query which is the music file of interest:

```java
import com.github.cschen1205.tensorflow.commons.FileUtils;
import com.github.cschen1205.tensorflow.search.models.AudioSearchEntry;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class MusicSearchEngineDemo {
    public static void main(String[] args){
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

```
