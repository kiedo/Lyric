package com.android.lyric;

/**
 *
 * @author apple
 */

public class LyricWordBean {

    private String word;
    private int startTime;
    private int durTime;


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

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

    @Override
    public String toString() {
        return "LyricWordBean{" +
                "word='" + word + '\'' +
                ", startTime=" + startTime +
                ", durTime=" + durTime +
                '}';
    }
}
