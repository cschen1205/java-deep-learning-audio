package com.github.cschen1205.tensorflow.search.models;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Date;

@Getter
@Setter
public class AudioSearchEntry {

    private String path;
    private float[] features;
    private long indexedTime;
    private double distance;
    private String error;

    public double getDistanceSq(float[] d) {
        double distanceSq = 0;
        for(int i=0; i < d.length; ++i){
            distanceSq += (d[i] - features[i]) * (d[i] - features[i]);
        }
        return distanceSq;
    }

    public AudioSearchEntry() {

    }

    public AudioSearchEntry(String path, float[] result) {
        this.indexedTime = new Date().getTime();
        this.features = result;
        this.path = path;
    }

    public boolean match(float[] d) {
        return getDistanceSq(d) == 0;
    }

    public String name() {
        return new File(path).getName();
    }

    public AudioSearchEntry makeCopy() {
        AudioSearchEntry result = new AudioSearchEntry(this.path, this.features);
        result.indexedTime = this.indexedTime;
        result.distance = this.distance;
        return result;
    }

    public static AudioSearchEntry createAlert(String error) {
        return new AudioSearchEntry().alert(error);
    }

    public AudioSearchEntry alert(String error) {
        this.error = error;
        return this;
    }

    public AudioSearchEntry withDistance(double distance) {
        this.distance = distance;
        return this;
    }
}
