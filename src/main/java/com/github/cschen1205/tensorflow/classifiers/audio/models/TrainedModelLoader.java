package com.github.cschen1205.tensorflow.classifiers.audio.models;

import java.io.IOException;
import java.io.InputStream;

public interface TrainedModelLoader {
    void load_model(InputStream inputStream) throws IOException;
}
