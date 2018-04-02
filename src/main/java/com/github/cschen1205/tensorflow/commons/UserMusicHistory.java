package com.github.cschen1205.tensorflow.commons;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserMusicHistory {
    private List<AudioMemo> history = new ArrayList<>();
    public void logAudio(String filePath){
        history.add(new AudioMemo(filePath));
    }

    public String head(int n) {
        StringBuilder sb = new StringBuilder();
        for(int i=history.size()-1; i >= history.size()-n && i >= 0; --i) {
            sb.append("# ").append(history.size() - i).append(": ").append(history.get(i).getAudioPath()).append("\n");
        }
        return sb.toString().trim();
    }
}
