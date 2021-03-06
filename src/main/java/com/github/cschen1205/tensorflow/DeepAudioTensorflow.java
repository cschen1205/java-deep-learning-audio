package com.github.cschen1205.tensorflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.cschen1205.tensorflow.commons.AudioMemo;
import com.github.cschen1205.tensorflow.commons.AudioRank;
import com.github.cschen1205.tensorflow.models.AudioClassifier;
import com.github.cschen1205.tensorflow.models.cifar10.Cifar10AudioClassifier;
import com.github.cschen1205.tensorflow.utils.ResourceUtils;
import com.github.cschen1205.tensorflow.search.models.AudioSearchEntry;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class DeepAudioTensorflow implements DeepAudio {
    private static final Logger logger = LoggerFactory.getLogger(DeepAudio.class);
    private AudioClassifier classifier;
    private List<AudioSearchEntry> database = new ArrayList<>();
    private String indexDbPath = "/tmp/music_index_db.json";

    public DeepAudioTensorflow() {
        InputStream inputStream = ResourceUtils.getInputStream("tf_models/cifar10.pb");
        Cifar10AudioClassifier classifier = new Cifar10AudioClassifier();
        try {
            classifier.load_model(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.classifier = classifier;
    }

    @Override
    public String predictMusicGenres(File audioFile) {
        return classifier.predict_audio(audioFile);
    }

    @Override
    public float[] encodeAudioFile(File audioFile) {
        return classifier.encode_audio(audioFile);
    }







    @Override
    public void purgeDb() {
        database.clear();
    }

    @Override
    public AudioSearchEntry index(File file) {
        logger.info("indexing file: " + file.getAbsolutePath());
        float[] result = classifier.encode_audio(file);
        AudioSearchEntry entry = new AudioSearchEntry(file.getAbsolutePath(), result);
        database.add(entry);
        return entry;
    }

    @Override
    public void indexMusicFiles(File[] files) {
        for(File f : files) {
            index(f);
        }
    }

    @Override
    public List<AudioSearchEntry> query(File file, int pageIndex, int pageSize) {
        return query(file, pageIndex, pageSize, false);
    }

    @Override
    public List<AudioSearchEntry> query(File file, int pageIndex, int pageSize, boolean skipPerfectMatch) {
        float[] d = classifier.encode_audio(file);
        List<AudioSearchEntry> temp = new ArrayList<>();
        for(AudioSearchEntry entry : database){
            if(!entry.match(d) || !skipPerfectMatch){
                temp.add(entry.makeCopy());
            }
        }
        for(AudioSearchEntry entry : temp){
            entry.setDistance(entry.getDistanceSq(d));
        }
        temp.sort(Comparator.comparingDouble(a -> a.getDistance()));

        List<AudioSearchEntry> result = new ArrayList<>();
        for(int i = pageIndex * pageSize; i < (pageIndex+1) * pageSize; ++i){
            if(i < temp.size()){
                result.add(temp.get(i));
            }
        }

        return result;
    }

    @Override
    public boolean loadMusicIndexDbIfExists() {
        File file = new File(indexDbPath);
        if(file.exists()){
            String json = null;
            try (Stream<String> stream = Files.lines(Paths.get(indexDbPath))) {

                //1. filter line 3
                //2. convert all content to upper case
                //3. convert it into a List
                json = stream
                        .filter(line -> !line.startsWith("line3"))
                        .map(String::toUpperCase)
                        .collect(Collectors.joining());

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(json != null) {
                database.clear();
                database.addAll(JSON.parseArray(json, AudioSearchEntry.class));
            }
            return true;
        }
        return false;

    }

    @Override
    public void saveMusicIndexDb() {
        File file = new File(indexDbPath);
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))){
            String json = JSON.toJSONString(database, SerializerFeature.BrowserCompatible);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<AudioSearchEntry> recommends(List<AudioMemo> userHistory, int k) {
        userHistory.sort((a, b) -> Long.compare(b.getEventTime(), a.getEventTime()));
        List<String> mostRecentHistory = new ArrayList<>();
        if(userHistory.size() > 60) {
            for(int i=0; i < 20; ++i) {
                AudioMemo memo = userHistory.get(i);
                String filePath = memo.getAudioPath();
                if(mostRecentHistory.indexOf(filePath) < 0) {
                    mostRecentHistory.add(filePath);
                }
            }
        } else if(userHistory.size() > 30) {
            for(int i=0; i < 10; ++i) {
                AudioMemo memo = userHistory.get(i);
                String filePath = memo.getAudioPath();
                if(mostRecentHistory.indexOf(filePath) < 0) {
                    mostRecentHistory.add(filePath);
                }
            }
        }

        Map<String, AudioRank> ranks = new HashMap<>();

        for(int i=0; i < mostRecentHistory.size(); ++i){
            String filePath = mostRecentHistory.get(i);
            double distance2 = (double)mostRecentHistory.size() / (i+1.0);

            File file = new File(filePath);
            List<AudioSearchEntry> similar_songs = query(file, 0, 10, true);

            for(AudioSearchEntry entry : similar_songs){
                double distance1 = Math.sqrt(entry.getDistance());

                double distance_mean = (distance1 * distance2) / (distance1 + distance2);

                AudioRank newRank = new AudioRank();
                newRank.setAudioPath(entry.getPath());
                newRank.setFeatures(entry.getFeatures());
                newRank.setDistance1(distance1);
                newRank.setDistance2(distance2);
                newRank.setMeanDistance(distance_mean);

                if(ranks.containsKey(entry.getPath())){
                    AudioRank rank = ranks.get(entry.getPath());
                    if(rank.getMeanDistance() < distance_mean){
                        ranks.put(entry.getPath(), newRank);
                    }
                } else {
                    ranks.put(entry.getPath(), newRank);
                }
            }
        }

        List<AudioRank> ranked = new ArrayList<>(ranks.values());
        ranked.sort(Comparator.comparingDouble(AudioRank::getMeanDistance));

        List<AudioSearchEntry> result = new ArrayList<>();
        for(int i=0; i < k; ++i){
            if(i < ranked.size()){
                result.add(ranked.get(i).toSearchEntry());
            }
        }
        return result;
    }
}
