package com.open_open.duanshipinluzhi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mabeijianxi.smallvideorecord2.DeviceUtils;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;
import com.mabeijianxi.smallvideorecord2.MediaRecorderActivity;
import com.mabeijianxi.smallvideorecord2.model.AutoVBRMode;
import com.mabeijianxi.smallvideorecord2.model.BaseMediaBitrateConfig;
import com.mabeijianxi.smallvideorecord2.model.MediaRecorderConfig;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private Button but;
    private String path;
    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initSmallVideo();
    permissionCheck();
    }

    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean permissionState = true;
            for (String permission : permissionManifest) {
                if (ContextCompat.checkSelfPermission(this, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionState = false;
                }
            }
            if (!permissionState) {
                ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
            } else {

            }
        } else {

        }
    }

    private void initView() {
        but = (Button) findViewById(R.id.but);

        but.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but:
getResult();
                break;
        }
    }

    public void  getResult(){
        go();
        /*// 录制
        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()

                .fullScreen(false)
                .smallVideoWidth(360)
                .smallVideoHeight(480)
                .recordTimeMax(6000)
                .recordTimeMin(1500)
                .maxFrameRate(20)
                .videoBitrate(600000)
                .captureThumbnailsTime(1)
                .build();
        MediaRecorderActivity.goSmallVideoRecorder(this, SendSmallVideoActivity.class.getName(), config);
// 选择本地视频压缩
        LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
        final LocalMediaConfig configs = buidler
                .setVideoPath(path)
                .captureThumbnailsTime(1)
                .doH264Compress(new AutoVBRMode())
                .setFramerate(15)
                .setScale(1.0f)
                .build();
        OnlyCompressOverBean onlyCompressOverBean = new LocalMediaCompress(configs).startCompress();*/
    }


    public static void initSmallVideo() {
        // 设置拍摄视频缓存路径
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
            } else {
                JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/mabeijianxi/");
            }
        } else {
            JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
        }
        // 初始化拍摄
        JianXiCamera.initialize(false, null);
    }


    public void go() {

        String width = "480";
        String height = "360";
        String maxFramerate = "20";
        String bitrate = "20";
        String maxTime = "4000";
        String minTime = "1500";
       // String s = spinner_need_full.getSelectedItem().toString();
        boolean needFull = true;

        BaseMediaBitrateConfig recordMode;
        BaseMediaBitrateConfig compressMode = null;


        recordMode = new AutoVBRMode();


//      FFMpegUtils.captureThumbnails("/storage/emulated/0/DCIM/mabeijianxi/1496455533800/1496455533800.mp4", "/storage/emulated/0/DCIM/mabeijianxi/1496455533800/1496455533800.jpg", "1");

        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
                .fullScreen(needFull)
                .smallVideoWidth(needFull?0:Integer.valueOf(width))
                .smallVideoHeight(Integer.valueOf(height))
                .recordTimeMax(Integer.valueOf(maxTime))
                .recordTimeMin(Integer.valueOf(minTime))
                .maxFrameRate(Integer.valueOf(maxFramerate))
                .videoBitrate(Integer.valueOf(bitrate))
                .captureThumbnailsTime(1)
                .build();
        MediaRecorderActivity.goSmallVideoRecorder(this, SendSmallVideoActivity.class.getName(), config);

    }
}
