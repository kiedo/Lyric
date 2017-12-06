package com.android.lyric;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.lyric.view.LyricRowView;

import java.util.List;


/**
 * @author apple
 */
public class LyricAdapter <T> extends BaseAdapter {
    private List<LyricRawBean> lrc;
    private Context context;

    public LyricAdapter(List<LyricRawBean> lrc, Context context) {
        this.lrc = lrc;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lrc.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    /**
     * 获取没一行歌词view
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LyricRowView mLyricRowView = new LyricRowView(context);
            mLyricRowView.setLyric(lrc.get(position));
            convertView = mLyricRowView;
        }
        return convertView;
    }


    /**
     * 暂无歌词view
     *
     * @param empty
     * @return
     */
    public View getView(String empty) {
        LyricRowView mLyricRowView = new LyricRowView(context);
        mLyricRowView.setText(empty);
        return mLyricRowView;
    }
}
