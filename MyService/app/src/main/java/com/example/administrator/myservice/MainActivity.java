package com.example.administrator.myservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //为日志工具设置标签
    private static String TAG = "MusicService";
    private Button btnStart,btnStop,btnBind,btnUnbind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //输出Toast消息和日志记录
        Toast.makeText(this, "MusicServiceActivity",
                Toast.LENGTH_SHORT).show();
        Log.e(TAG, "MusicServiceActivity");
        initView();
    }

    private void initView() {
        btnStart = (Button)findViewById(R.id.startMusic);
         btnStop = (Button)findViewById(R.id.stopMusic);
         btnBind = (Button)findViewById(R.id.bindMusic);
         btnUnbind = (Button)findViewById(R.id.unbindMusic);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnBind.setOnClickListener(this);
        btnUnbind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startMusic:
                //开始服务
                Intent Startintent=new Intent(this,MyServices.class);
                startService(Startintent);
                break;
            case R.id.stopMusic:
                //停止服务
                Intent Stopintent=new Intent(this,MyServices.class);
                stopService(Stopintent);
                break;
            case R.id.bindMusic:
                //绑定服务
                Intent bindintent=new Intent(this,MyServices.class);
                bindService(bindintent,conn, Context.BIND_AUTO_CREATE);
                break;
            case R.id.unbindMusic:
                //解绑服务
                Intent unbindintent=new Intent(this,MyServices.class);
                unbindService(conn);
                break;
        }
    }

    //定义服务链接对象
    final ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(MainActivity.this, "MusicServiceActivity onSeviceDisconnected"
                    , Toast.LENGTH_SHORT).show();
            Log.e(TAG, "MusicServiceActivity onSeviceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(MainActivity.this, "MusicServiceActivity onServiceConnected"
                    ,Toast.LENGTH_SHORT).show();
            Log.e(TAG, "MusicServiceActivity onServiceConnected");
        }
    };
}
