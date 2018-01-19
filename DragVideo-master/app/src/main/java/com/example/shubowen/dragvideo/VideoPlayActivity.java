package com.example.shubowen.dragvideo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;

/**
 * 疏博文 新建于 2017/7/27.
 * 邮箱： shubw@icloud.com
 * 描述：请添加此文件的描述
 */

public class VideoPlayActivity extends AppCompatActivity {

    private VideoView mVideoView;
    private String mUrl;
    private boolean failplay = false;
//    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_play);

        mVideoView = (VideoView) findViewById(R.id.video_view);
        MediaController controller = new MediaController(this);
        Activity activity = new VideoPlayActivity();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mVideoView.setMediaController(controller);
        mUrl = getIntent().getStringExtra("url");
        final int position = getIntent().getIntExtra("position", 0);

        mVideoView.setVideoPath(mUrl);
        mVideoView.seekTo(position);
        mVideoView.start();
        mVideoView.pause();

        SeekBar seekBar = new SeekBar(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mVideoView.start();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(100);
            }
        });
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });
        //窗口全屏切换
//        initqiehuan();
        controller.setMediaPlayer(mVideoView);

    }

    private void initqiehuan() {
        if (!failplay) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mVideoView.setLayoutParams(layoutParams);
            failplay = true;
        } else {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(320, 240);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            mVideoView.setLayoutParams(lp);
            failplay = false;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }

    public static void go(Activity activity, String url, int position) {
        //先停止小窗口视频
        VideoWidowService.stop(activity);

        Intent intent = new Intent(activity, VideoPlayActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("position", position);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mVideoView.isPlaying())
            VideoWidowService.go(this, mUrl, mVideoView.getCurrentPosition());
        super.onBackPressed();
    }


}
