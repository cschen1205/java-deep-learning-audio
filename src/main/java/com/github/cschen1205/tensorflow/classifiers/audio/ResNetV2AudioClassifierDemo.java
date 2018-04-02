package com.github.cschen1205.tensorflow.classifiers.audio;

import com.github.cschen1205.tensorflow.classifiers.audio.models.resnet.ResNetV2AudioClassifier;
import com.github.cschen1205.tensorflow.commons.FileUtils;
import com.github.cschen1205.tensorflow.classifiers.audio.utils.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class ResNetV2AudioClassifierDemo {


    public static void main(String[] args) throws IOException {

        ResNetV2AudioClassifier classifier = new ResNetV2AudioClassifier();

        InputStream inputStream = ResourceUtils.getInputStream("tf_models/resnet-v2.pb");
        classifier.load_model(inputStream);

        List<String> paths = FileUtils.getAudioFilePaths();

        Collections.shuffle(paths);

        for (String path : paths) {
            System.out.println("Predicting " + path + " ...");
            File f = new File(path);
            String label = classifier.predict_audio(f);

            System.out.println("Predicted: " + label);
        }
    }
}
