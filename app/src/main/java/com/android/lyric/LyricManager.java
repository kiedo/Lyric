package com.android.lyric;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * @author apple
 */

public class LyricManager {

    private LyricFileRead mLyricFileRead;
    private Context context;
    private ParseLyric mParseLyric;

    public LyricManager(Context context) {
        this.context = context;
    }

    private LyricFileRead generateLyricUtil() {
        return mLyricFileRead == null ? mLyricFileRead = new LyricFileRead() : mLyricFileRead;
    }

    private ParseLyric generateParseLyric() {
        return mParseLyric == null ? mParseLyric = new ParseLyric() : mParseLyric;
    }


    public List<LyricRawBean> openLyrics() {
        generateLyricUtil().openLyric(context);
        List<String> lyricsListStr = generateLyricUtil().getLyrics();
        List<LyricRawBean> lyricParseList = new ArrayList<>();
        if (lyricsListStr != null && !lyricsListStr.isEmpty()) {
            try {
                lyricParseList.clear();
                generateParseLyric().setData(lyricsListStr);
                lyricParseList.addAll(generateParseLyric().getmLyricRawList());
            } catch (Exception e) {
                e.printStackTrace();
                lyricParseList.clear();
            }
        }
        return lyricParseList;

    }

    public void onDetachedFromWindow() {
        if (mLyricFileRead != null) {
            mLyricFileRead.onDetachedFromWindow();
            mLyricFileRead = null;
        }
        if (mParseLyric != null) {
            mParseLyric.onDetachedFromWindow();
            mLyricFileRead = null;
        }
    }
}
