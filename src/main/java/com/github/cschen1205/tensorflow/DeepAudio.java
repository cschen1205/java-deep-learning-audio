package com.github.cschen1205.tensorflow;

import com.github.cschen1205.tensorflow.search.models.AudioSearchEntry;

import java.io.File;
import java.util.List;

public interface DeepAudio {

    void purgeDb();

    AudioSearchEntry index(File file);

    void indexAll(File[] files);

    List<AudioSearchEntry> query(File file, int pageIndex, int pageSize);

    List<AudioSearchEntry> query(File file, int pageIndex, int pageSize, boolean skipPerfectMatch);

    boolean loadIndexDbIfExists();

    void saveIndexDb();
}
