package com.android.lyric.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.lyric.LyricAdapter;
import com.android.lyric.LyricManager;
import com.android.lyric.LyricRawBean;

import java.util.List;


/**
 * @author apple
 */

public class LyricView extends ScrollView {

    final String TAG = getClass().getSimpleName();

    private Paint mLrcPait = new Paint(Paint.ANTI_ALIAS_FLAG);

    IPlayTimeListener mIPlayTimeListener;

    private LyricManager mLyricManager;
    private LyricAdapter mLyricAdapter;
    private LyricRowView preView;
    private LinearLayout mContentView;
    private String emptyText = "暂无歌词";
    private boolean isStopScrolling = false;

    public LyricView(Context context) {
        this(context, null);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFillViewport(true);
        removeAllViews();
        mContentView = new LinearLayout(getContext());
        mContentView.setGravity(Gravity.CENTER_VERTICAL);
        mContentView.setOrientation(LinearLayout.VERTICAL);
        addView(mContentView, new ScrollView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mLrcPait.setColor(Color.BLACK);
        mLrcPait.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getContext().getResources().getDisplayMetrics()));

    }

    private LyricManager generateLyricManager() {
        return mLyricManager == null ? mLyricManager = new LyricManager(getContext()) : mLyricManager;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mContentView.setPadding(0, getMeasuredHeight() / 2, 0, getMeasuredHeight() / 2);
    }


    public void setAdapter(BaseAdapter adapter) {


        if (adapter instanceof LyricAdapter) {
            mLyricAdapter = (LyricAdapter) adapter;
            int count = mLyricAdapter.getCount();
            if (count == 0) {
                mContentView.addView(mLyricAdapter.getView(emptyText));
            } else {
                for (int i = 0; i < count; i++) {
                    LyricRowView lyric = (LyricRowView) mLyricAdapter.getView(i, null, null);
                    lyric.setDirectionLTR();
                    lyric.setLyricView(this);
                    mContentView.addView(lyric);
                }

            }

        }
    }


    public List<LyricRawBean> getLrc() {
        return generateLyricManager().openLyrics();
    }


    public void notifyLyric() {

        if (mLyricAdapter != null) {
            int count = mLyricAdapter.getCount();
            for (int i = 0, s = count; i < s; i++) {
                LyricRowView lyricRowView = (LyricRowView) mContentView.getChildAt(i);
                if (lyricRowView.isCurrentLyricPlay(getplayTime())) {
                    if (lyricRowView == preView) {
                        lyricRowView.invalidate();
                    } else {
                        preView = lyricRowView;
                        notifyDataSetChanged();
                        if (!isStopScrolling) {
                            smoothScrollTo(0, (int) (lyricRowView.getY() - getMeasuredHeight() / 2));
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isStopScrolling = true;
                break;

            case MotionEvent.ACTION_UP:
                isStopScrolling = false;
                break;
            default:
        }
        return super.onTouchEvent(ev);
    }

    private void notifyDataSetChanged() {
        if (mContentView != null) {
            int childCount = mContentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                mContentView.getChildAt(i).invalidate();
            }
        }
    }


    public int getplayTime() {
        if (mIPlayTimeListener != null) {
            return mIPlayTimeListener.callTime();
        }
        throw new RuntimeException("必须设置播放时间回调 IPlayTimeListener ");
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mLyricManager != null) {
            mLyricManager.onDetachedFromWindow();
            mLyricManager = null;
        }
    }

    public void setPlayTimeListener(IPlayTimeListener listener) {
        mIPlayTimeListener = listener;
    }

    public  interface IPlayTimeListener {
        int callTime();
    }

}
