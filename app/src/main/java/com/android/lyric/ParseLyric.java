package com.android.lyric;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author apple
 */

public class ParseLyric {

    final String TAG = getClass().getSimpleName();

    private String title = "";
    private String artist = "";
    private String album = "";
    private String lrcMaker = "";

    private List<LyricRawBean> mLyricRawList = new ArrayList<>();
    private StringBuilder stringBuilder = new StringBuilder();

    public void setData(List<String> lyricsStr) {

        for (int i = 0, l = lyricsStr.size(); i < l; i++) {
            String s = lyricsStr.get(i);
            if (s == null || "".equals(s.trim())) {
                continue;
            }
            if (null == title || "".equals(title.trim())) {
                Pattern pattern = Pattern.compile("\\[ti:(.+?)\\]");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    title = matcher.group(1);
                    continue;
                }
            }
            if (null == artist || "".equals(artist.trim())) {
                Pattern pattern = Pattern.compile("\\[ar:(.+?)\\]");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    artist = matcher.group(1);
                    continue;
                }
            }
            if (null == album || "".equals(album.trim())) {
                Pattern pattern = Pattern.compile("\\[al:(.+?)\\]");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    album = matcher.group(1);
                    continue;
                }
            }
            if (null == lrcMaker || "".equals(lrcMaker.trim())) {
                Pattern pattern = Pattern.compile("\\[by:(.+?)\\]");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    lrcMaker = matcher.group(1);
                    continue;
                }
            }
            parseLineTime(s);

        }
        acountTime();
    }

    private List<LyricRawBean> generateList() {
        return mLyricRawList == null ? mLyricRawList = new ArrayList<>() : mLyricRawList;

    }

    /**
     * 解析一行歌词时间
     *
     * @param lrcLine
     */
    private void parseLineTime(String lrcLine) {
        if (lrcLine.contains("]")) {
            generateList();
            String[] split = lrcLine.split("]");
            if (split.length > 1) {
                if (split[0].contains("[")) {
                    split[0] = split[0].split("\\[")[1];
                }
            }
            String timeStr = split[0];
            String[] times = timeStr.split(",");
            String startTime = times[0];
            String duringTime = times[1];
            LyricRawBean mLyricParseBean = new LyricRawBean();
            mLyricRawList.add(mLyricParseBean);
            mLyricParseBean.setDurTime(parseInt(duringTime));
            mLyricParseBean.setStartTime(parseInt(startTime));
            parseWord(split[1], mLyricParseBean);

        }
    }

    private StringBuilder generateStringBuilder() {
        return stringBuilder == null ? stringBuilder = new StringBuilder() : stringBuilder;
    }


    private void parseWord(String lrc, LyricRawBean mLyricParseBean) {
        String[] split = lrc.split("\\)");
        generateStringBuilder();
        stringBuilder.setLength(0);
        for (int i = 0, l = split.length; i < l; i++) {
            String word = split[i];
            if("پ".equals(word)){
                break;
            }
            String[] wordTime = word.split("\\(");
            String wordLrc = wordTime[0];
            String[] times = wordTime[1].split(",");
            String startTime = times[0];
            String durTime = times[1];
            LyricWordBean lyricWordBean = new LyricWordBean();
            lyricWordBean.setWord(wordLrc);
            lyricWordBean.setStartTime(parseInt(startTime));
            lyricWordBean.setDurTime(parseInt(durTime));
            mLyricParseBean.getWords().add(lyricWordBean);
            stringBuilder.append(wordLrc);
        }
        mLyricParseBean.setLinelrc(stringBuilder.toString());

//        log(lrc);
    }

    private void acountTime() {

        for (int i = 0, l = generateList().size(); i < l; i++) {
            LyricRawBean lyricRawBean = mLyricRawList.get(i);
            if (i < l - 1) {
                LyricRawBean lyricRawNext = mLyricRawList.get(i + 1);
                lyricRawBean.setEndTime(lyricRawNext.getStartTime());
            }
        }
    }


    public List<LyricRawBean> getmLyricRawList() {
        return mLyricRawList;
    }


    private int parseInt(String time) {
        return Integer.parseInt(time);
    }

    public void onDetachedFromWindow() {
        mLyricRawList.clear();
        mLyricRawList = null;
    }
}
