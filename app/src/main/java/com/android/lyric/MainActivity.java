package com.android.lyric;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.android.lyric.view.LyricView;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    LyricView lyric;
    List<LyricRawBean> lrc;
    LyricAdapter lyricAdapter;

    public static int playTime = 20000;

    Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    playTime += 100;
                    if (playTime > 88840) {
                        playTime = 20000;
                    }
                    mhandler.sendEmptyMessageDelayed(1, 100);
                    lyric.notifyLyric();
                    break;

                default:

            }

            return false;
        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        mhandler.removeMessages(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeMessages(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playTime = 20000;
        lyric = (LyricView) findViewById(R.id.lyric);

        lrc = lyric.getLrc();
        if (lrc != null) {
            lyricAdapter = new LyricAdapter(lrc, getApplication());
            lyric.setAdapter(lyricAdapter);
            mhandler.sendEmptyMessageDelayed(1, 10);
        }

        lyric.setPlayTimeListener(new LyricView.IPlayTimeListener() {
            @Override
            public int callTime() {
                return playTime;
            }
        });
        mhandler.sendEmptyMessageDelayed(11, 5000);

    }

}
