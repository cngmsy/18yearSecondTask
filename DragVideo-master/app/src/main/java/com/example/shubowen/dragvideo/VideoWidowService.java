package com.example.shubowen.dragvideo;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 疏博文 新建于 2017/7/27.
 * 邮箱： shubw@icloud.com
 * 描述：请添加此文件的描述
 */

public class VideoWidowService extends Service {

    private VideoView videoViewRemote;
    private WindowManager mWm;
    private WindowManager.LayoutParams mLp;
    private DragLayout mVideoWidow;

    private boolean mVideoPreparing;

    private boolean hasRemoved = false;
    private View mTvTip;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final String url = intent.getStringExtra("url");
        final int position = intent.getIntExtra("position", 0);

        if (!TextUtils.isEmpty(url)) {
            playOnWindow(url, position);
        } else {
            releaseVideoWidow();
        }

        return START_STICKY;
    }

    private void playOnWindow(String url, final int position) {
        if (TextUtils.isEmpty(url)) {
            Log.e("playOnWindow", "url is empty");
            return;
        }

        mVideoPreparing = true;

        if (null == mVideoWidow) {
            createAndInitWidow();
        }
        if (null == mLp) {
            createAndInitLayoutParams();
        }

        mTvTip.setVisibility(View.VISIBLE);


        if (null == mWm)
            mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        hasRemoved = false;
        mWm.addView(mVideoWidow, mLp);

        videoViewRemote.setVideoPath(url);
        videoViewRemote.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoPreparing = false;
                mTvTip.setVisibility(View.GONE);
                videoViewRemote.seekTo(position);
            }
        });

        videoViewRemote.start();
    }

    private void createAndInitLayoutParams() {
        mLp = new WindowManager.LayoutParams();
        mLp.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        mLp.x = (int) (getResources().getDisplayMetrics().density * 50);
        mLp.y = 0;
        mLp.type = Build.VERSION.SDK_INT >= 25 ?
                WindowManager.LayoutParams.TYPE_PHONE : WindowManager.LayoutParams.TYPE_TOAST;
        mLp.height = -2;
        mLp.width = -2;

        mLp.format = PixelFormat.TRANSLUCENT;

        mLp.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    private void createAndInitWidow() {
        mVideoWidow = (DragLayout) LayoutInflater.from(this).inflate(R.layout.lay_float_video, null);
        mVideoWidow.setDragListener(new DragLayout.OnDragListener() {
            @Override
            public void onDrag(float offsetX, float offsetY) {
                mLp.x += offsetX;
                mLp.y += offsetY;
                mWm.updateViewLayout(mVideoWidow, mLp);
            }
        });
        videoViewRemote = (VideoView) mVideoWidow.findViewById(R.id.video_view);
        videoViewRemote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VideoWidowService.this, "点击了视频", Toast.LENGTH_SHORT).show();
            }
        });
        videoViewRemote.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseVideoWidow();
            }
        });

        mTvTip = mVideoWidow.findViewById(R.id.tv_tip);

        View removeIcon = mVideoWidow.findViewById(R.id.iv_remove);
        removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseVideoWidow();
            }
        });
    }

    private void releaseVideoWidow() {
        if (null != videoViewRemote) {
            if (mVideoPreparing || videoViewRemote.isPlaying()) {
                videoViewRemote.stopPlayback();
            }
        }

        if (null != mVideoWidow && !hasRemoved)
            mWm.removeViewImmediate(mVideoWidow);

        hasRemoved = true;
    }

    /**
     * 开启视频悬浮窗
     *
     * @param activity
     * @param url      视频地址
     * @param position 视频播放的位置
     */
    public static void go(Activity activity, String url, int position) {
        Intent intent = new Intent(activity, VideoWidowService.class);
        intent.putExtra("url", url);
        intent.putExtra("position", position);
        activity.startService(intent);
    }

    /**
     * 停止并移除视频悬浮窗
     *
     * @param context
     */
    public static void stop(Context context) {
        Intent intent = new Intent(context, VideoWidowService.class);
        context.startService(intent);
    }

    /**
     * 终止视频悬浮窗服务，一般在应用结束后使用
     *
     * @param activity
     */
    public static void release(Activity activity) {
        Intent intent = new Intent(activity, VideoWidowService.class);
        activity.stopService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseVideoWidow();
    }
}
