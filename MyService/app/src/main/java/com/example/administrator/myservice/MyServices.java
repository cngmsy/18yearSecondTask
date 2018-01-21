package com.example.administrator.myservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyServices extends Service {

    //为日志工具设置标签
    private static String TAG = "MusicService";
    //定义音乐播放器变量
    private MediaPlayer mPlayer;
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "MusicSevice onCreate()"
                , Toast.LENGTH_SHORT).show();
        Log.e(TAG, "MusicSerice onCreate()");

        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.quanshijiezhidaowoaini);
        //设置可以重复播放
        mPlayer.setLooping(true);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "MusicSevice onStart()"
                , Toast.LENGTH_SHORT).show();
        Log.e(TAG, "MusicSerice onStart()");

        mPlayer.start();
        
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "MusicSevice onDestroy()"
                , Toast.LENGTH_SHORT).show();
        Log.e(TAG, "MusicSerice onDestroy()");

        mPlayer.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "MusicSevice onBind()"
                , Toast.LENGTH_SHORT).show();
        Log.e(TAG, "MusicSerice onBind()");

        mPlayer.start();
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "MusicSevice onUnbind()"
                , Toast.LENGTH_SHORT).show();
        Log.e(TAG, "MusicSerice onUnbind()");

        mPlayer.stop();
        return super.onUnbind(intent);
    }



}

