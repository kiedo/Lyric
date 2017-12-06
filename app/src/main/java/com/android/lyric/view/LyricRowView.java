package com.android.lyric.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.android.lyric.LyricRawBean;
import com.android.lyric.LyricWordBean;

import java.util.ArrayList;
import java.util.List;


/**
 * @author apple
 */

public class LyricRowView extends View {

    final String TAG = getClass().getSimpleName();

    private final float DEFAULT_PPERCENT = 0.7F;
    /**
     * 歌词文本显示的最大宽度
     */
    private float defaultDisplayLrcWodth;

    /**
     * 每一行没经过折行计算的歌词
     */
    private LyricRawBean mLyricRawBean;
    private Paint mLrcPait = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mLrcPlayedPait = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float textLineHeight, textBaseLine;
    private StringBuilder stringBuilder;
    private RectF mRect = new RectF();
    /**
     * 用于折行，装多行的数据
     */
    private List<List<LyricWordBean>> mraws = new ArrayList<>();
    /**
     * 多行的时候，装每一行歌词，便于下面的歌词定位坐标
     */
    private List<String> mRawLrcStr = new ArrayList<>();

    private String empty = "暂无歌词";
    private LyricView lyricView;

    public LyricRowView(Context context) {
        this(context, null);
    }

    public LyricRowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricRowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setLayoutDirection(LAYOUT_DIRECTION_LTR);
    }

    @Override
    public void setLayoutDirection(int layoutDirection) {
        super.setLayoutDirection(layoutDirection);
    }

    @Override
    public int getLayoutDirection() {
        return super.getLayoutDirection();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = widthMeasureSpec, h = heightMeasureSpec;

        mraws.clear();
        mRawLrcStr.clear();
        if (mLyricRawBean != null) {
            defaultDisplayLrcWodth = getMeasuredWidth() * DEFAULT_PPERCENT;
            if (defaultDisplayLrcWodth > 0) {
                /**
                 * 多行显示，装每一行的单个歌词实体
                 */
                List<LyricWordBean> rowWords = new ArrayList<>();
                List<LyricWordBean> words = mLyricRawBean.getWords();
                generateBuilder();
                stringBuilder.setLength(0);
                for (int i = 0, l = words.size(); i < l; i++) {
                    LyricWordBean lyricWordBean = words.get(i);
                    float textWidth = measureText(stringBuilder.toString() + lyricWordBean.getWord());
                    if (textWidth > defaultDisplayLrcWodth) {
                        mRawLrcStr.add(stringBuilder.toString());
                        stringBuilder.setLength(0);
                        mraws.add(rowWords);
                        rowWords = new ArrayList<LyricWordBean>();
                    }
                    rowWords.add(lyricWordBean);
                    stringBuilder.append(lyricWordBean.getWord());
                }
                mraws.add(rowWords);
                mRawLrcStr.add(stringBuilder.toString());
                h = (int) textLineHeight * mraws.size();
            }
        } else {
            h = (int) textLineHeight;
        }
        setMeasuredDimension(w, h);
    }


    private void init() {
        mLrcPait.setColor(Color.BLACK);
        mLrcPait.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getContext().getResources().getDisplayMetrics()));
        mLrcPlayedPait.setColor(Color.RED);
        mLrcPlayedPait.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getContext().getResources().getDisplayMetrics()));

        Paint.FontMetrics fontMetrics = mLrcPait.getFontMetrics();
        textLineHeight = fontMetrics.bottom - fontMetrics.top;
        textBaseLine = Math.abs(fontMetrics.top);
    }

    public void setLyric(LyricRawBean lyricRaw) {
        mLyricRawBean = lyricRaw;
        invalidate();
    }

    public void setText(String text) {
        empty = text;
    }

    /**
     * 设置逐字歌词逐字方向，左边到右边
     *
     * @return
     */
    public LyricRowView setDirectionLTR() {
        setLayoutDirection(LAYOUT_DIRECTION_LTR);
        return this;
    }

    /**
     * 设置逐字歌词逐字方向，右边到左边
     *
     * @return
     */
    public LyricRowView setDirectionRTL() {
        setLayoutDirection(LAYOUT_DIRECTION_RTL);
        return this;
    }

    /**
     * 设置普通文本的颜色
     *
     * @param color
     * @return
     */
    public LyricRowView setNorTextColor(int color) {
        mLrcPait.setColor(color);
        return this;
    }

    /**
     * 设置高亮文本颜色
     */
    public LyricRowView setLightHighColor(int color) {
        mLrcPlayedPait.setColor(color);
        return this;
    }

    /**
     * 设置文本大小
     *
     * @param textSize
     * @return
     */
    public LyricRowView setNorTextSize(int textSize) {
        mLrcPait.setTextSize(textSize);
        return this;
    }

    /**
     * 设置文本大小
     *
     * @param size
     * @return
     */
    public LyricRowView setLightHighTextSize(int size) {
        mLrcPlayedPait.setTextSize(size);
        return this;
    }

    public StringBuilder generateBuilder() {
        return stringBuilder == null ? stringBuilder = new StringBuilder() : stringBuilder;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLyricRawBean != null && !TextUtils.isEmpty(mLyricRawBean.getLinelrc())) {
            int startx = 0, w = getMeasuredWidth();
            List<List<LyricWordBean>> mraws = this.mraws;
            for (int i = 0, s = mraws.size(); i < s; i++) {
                List<LyricWordBean> lyricWordBeans = mraws.get(i);
                String rawStr = mRawLrcStr.get(i);
                float rawTextW = measureText(rawStr);
                startx = (int) ((w - rawTextW) / 2);
                float playedWidth = 0f;
                //绘制正常颜色歌词
                drawText(canvas, rawStr, startx, rawTextW, i, mLrcPait);
                /**
                 * 必须在当前行的时间内，采取计算绘制高亮
                 */
                if (isCurrentLyricPlay(getPlayTime())) {
                    for (int j = 0, l = lyricWordBeans.size(); j < l; j++) {
                        LyricWordBean lyricWordBean = lyricWordBeans.get(j);
                        float wordW = measureText(lyricWordBean.getWord());
                        if (getPlayTime() > lyricWordBean.getStartTime()) {
                            playedWidth += (getPlayTime() - lyricWordBean.getStartTime()) * (wordW / lyricWordBean.getDurTime());
                        }
                    }
                    //绘制高亮歌词文本
                    drawText(canvas, rawStr, startx, playedWidth, i, mLrcPlayedPait);
                }
            }

        } else {
            if (!TextUtils.isEmpty(empty)) {
                float rawTextW = measureText(empty);
                int startx = (int) ((getMeasuredWidth() - rawTextW) / 2);
                drawText(canvas, empty, startx, rawTextW, 0, mLrcPait);
            }

        }

    }


    /**
     * @param startx
     * @param playedWidth
     * @return
     */
    private int getRectRight(int startx, float playedWidth, String newline) {
        if (isLTR()) {
            return (int) (startx + playedWidth);
        } else {
            return (int) (startx + measureText(newline));

        }
    }

    /**
     * 绘制开始的left
     *
     * @param startx
     * @param playedWidth
     * @return
     */
    private int getRectLeft(int startx, float playedWidth, String newline) {

        if (isLTR()) {
            return startx;
        } else {
            return (int) (startx + measureText(newline) - playedWidth);
        }
    }

    /**
     * 判断绘制方向
     *
     * @return
     */
    private boolean isLTR() {
        return getLayoutDirection() == LAYOUT_DIRECTION_LTR;
    }

    /**
     * 从播放器获取当前播放时间
     *
     * @return
     */
    private int getPlayTime() {
        if(lyricView != null){
            return lyricView.getplayTime();
        }
        return 0;
    }

    /**
     * 测量歌词宽
     *
     * @param text
     * @return
     */
    private float measureText(String text) {
        return mLrcPait.measureText(text);
    }

    /**
     * 是否播放当前歌词
     *
     * @param playtime 播放时间
     * @return true正在播放当前歌词
     */
    public boolean isCurrentLyricPlay(int playtime) {
        return playtime > mLyricRawBean.getStartTime() && playtime < mLyricRawBean.getEndTime();
    }

    /**
     * 在rect区域内绘制歌词
     *
     * @param canvas
     * @param newline
     * @param startX
     * @param index
     * @param mPait
     */
    void drawText(Canvas canvas, String newline, int startX, float playedWidth, int index, Paint mPait) {

        int left = getRectLeft(startX, playedWidth, newline);
        int right = getRectRight(startX, playedWidth, newline);

        RectF mRectTemp = this.mRect;
        mRectTemp.set(left,
                index * textLineHeight,
                right,
                (index + 1) * textLineHeight);
        canvas.save();
        canvas.clipRect(mRectTemp);
        canvas.drawText(newline, startX, index * textLineHeight + textBaseLine, mPait);
        canvas.restore();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mraws.clear();
        mRawLrcStr.clear();
        lyricView = null;
    }

    public void setLyricView(LyricView lyricView) {
        this.lyricView = lyricView;
    }
}
