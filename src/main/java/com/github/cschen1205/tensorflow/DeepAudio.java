package com.github.cschen1205.tensorflow;

import com.github.cschen1205.tensorflow.search.models.AudioSearchEntry;

import java.io.File;
import java.util.List;

public interface DeepAudio {

    String predictMusicGenres(File audioFile);

    float[] encodeAudioFile(File audioFile);

    void purgeDb();

    AudioSearchEntry index(File file);

    void indexMusicFiles(File[] files);

    List<AudioSearchEntry> query(File file, int pageIndex, int pageSize);

    List<AudioSearchEntry> query(File file, int pageIndex, int pageSize, boolean skipPerfectMatch);

    boolean loadMusicIndexDbIfExists();

    void saveMusicIndexDb();
}
