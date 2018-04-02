package com.github.cschen1205.tensorflow.search;

import com.github.cschen1205.tensorflow.commons.FileUtils;
import com.github.cschen1205.tensorflow.search.models.AudioSearchEngine;
import com.github.cschen1205.tensorflow.search.models.AudioSearchEngineImpl;
import com.github.cschen1205.tensorflow.search.models.AudioSearchEntry;

import java.io.File;
import java.util.List;

public class AudioSearchEngineDemo {
    public static void main(String[] args){
        AudioSearchEngine searchEngine = new AudioSearchEngineImpl();
        if(!searchEngine.loadIndexDbIfExists()) {
            searchEngine.indexAll(FileUtils.getAudioFiles());
            searchEngine.saveIndexDb();
        }

        int pageIndex = 0;
        int pageSize = 20;
        boolean skipPerfectMatch = true;
        for(File f : FileUtils.getAudioFiles()) {
            System.out.println("querying similar music to " + f.getName());
            List<AudioSearchEntry> result = searchEngine.query(f, pageIndex, pageSize, skipPerfectMatch);
            for(int i=0; i < result.size(); ++i){
                System.out.println("# " + i + ": " + result.get(i).getPath() + " (distSq: " + result.get(i).getDistance() + ")");
            }
        }
    }
}
