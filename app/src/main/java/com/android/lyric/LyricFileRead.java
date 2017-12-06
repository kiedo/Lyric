package com.android.lyric;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取歌词文本
 *
 */

public class LyricFileRead {


    private List<String> lyrics = new ArrayList<String>();
    private InputStream io;

    private BufferedReader bufferedReader;

    public void openLyric(Context context) {
        try {
            AssetManager assets = context.getAssets();
//            io = assets.open("lyric.txt");
            io = assets.open("lyric1.txt");
            readLyric();
        } catch (Exception e) {
            e.printStackTrace();
            if (lyrics != null) {
                lyrics.clear();
            }
        } finally {
            IOutil.close(io);
            IOutil.close(bufferedReader);
        }
    }

    public List<String> getLyrics() {
        return lyrics;
    }

    private void readLyric() throws IOException {

        bufferedReader = new BufferedReader(new InputStreamReader(io));
        if (lyrics == null) {
            lyrics = new ArrayList<>();
        }
        lyrics.clear();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lyrics.add(line);
        }
    }

    public void onDetachedFromWindow() {
        lyrics.clear();
        lyrics = null;
    }
}
