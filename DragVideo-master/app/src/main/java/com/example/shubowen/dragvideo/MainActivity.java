package com.example.shubowen.dragvideo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, VideoWidowService.class);
//        intent.putExtra("url", "http://video.jiecao.fm/11/23/xin/%E5%81%87%E4%BA%BA.mp4");
//        intent.putExtra("position", 0);
//        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(this, VideoWidowService.class);
        stopService(intent);
    }

    public static void goWithVideo(Activity activity, String url, int position) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("position", position);
        activity.startActivity(intent);
    }

//    public void skip(View view) {
//        Intent intent = new Intent(this, TestActivity.class);
//        startActivity(intent);
//    }

    public void play(View view) {
        VideoPlayActivity.go(this, "http://video.jiecao.fm/11/23/xin/%E5%81%87%E4%BA%BA.mp4", 0);
    }
}
