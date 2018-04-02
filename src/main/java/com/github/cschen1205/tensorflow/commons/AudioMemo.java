package com.github.cschen1205.tensorflow.commons;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AudioMemo {
    private String audioPath;
    private long eventTime;

    public AudioMemo() {

    }

    public AudioMemo(String filePath){
        this.audioPath = filePath;
        eventTime = new Date().getTime();
    }
}
