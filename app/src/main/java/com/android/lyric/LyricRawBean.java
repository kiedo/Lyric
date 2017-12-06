package com.android.lyric;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apple
 */

public class LyricRawBean {

    private String linelrc;
    private int startTime;
    private int endTime;
    private int durTime;
    private List<LyricWordBean> words = new ArrayList<>();

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getDurTime() {
        return durTime;
    }

    public void setDurTime(int durTime) {
        this.durTime = durTime;
    }

    public List<LyricWordBean> getWords() {

        return words == null ? words = new ArrayList<>() : words;
    }

    public String getLinelrc() {
        return linelrc;
    }

    public void setLinelrc(String linelrc) {
        this.linelrc = linelrc;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "LyricRawBean{" +
                "Linelrc='" + linelrc + '\'' +
                ", startTime=" + startTime +
                ", durTime=" + durTime +
                ", words=" + words +
                '}';
    }
}
