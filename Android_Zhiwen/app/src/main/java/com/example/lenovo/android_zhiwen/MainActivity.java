package com.example.lenovo.android_zhiwen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.tfiv1)
    TouchFingerImageView tfiv1;
    @InjectView(R.id.tfiv2)
    TouchFingerImageView tfiv2;
    @InjectView(R.id.tfiv3)
    TouchFingerImageView tfiv3;
    @InjectView(R.id.tfiv4)
    TouchFingerImageView tfiv4;
    @InjectView(R.id.tfiv5)
    TouchFingerImageView tfiv5;
    @InjectView(R.id.tfiv6)
    TouchFingerImageView tfiv6;
    @InjectView(R.id.tfiv7)
    TouchFingerImageView tfiv7;
    @InjectView(R.id.tfiv8)
    TouchFingerImageView tfiv8;
    @InjectView(R.id.tfiv9)
    TouchFingerImageView tfiv9;
    @InjectView(R.id.tfiv10)
    TouchFingerImageView tfiv10;
    @InjectView(R.id.tfiv11)
    TouchFingerImageView tfiv11;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.tfiv1, R.id.tfiv2, R.id.tfiv3, R.id.tfiv4, R.id.tfiv5, R.id.tfiv6, R.id.tfiv7, R.id.tfiv8, R.id.tfiv9, R.id.tfiv10, R.id.tfiv11})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tfiv1:
                intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", "https://www.baidu.com/");
                startActivity(intent);
                break;
            case R.id.tfiv2:
                intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", "http://www.daizhuzai.com/");
                startActivity(intent);
                break;
            case R.id.tfiv3:
                intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", "http://www.booktxt.net/6_6453/");
                startActivity(intent);
                break;
            case R.id.tfiv4:
                intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", "https://www.88dushu.com/xiaoshuo/38/38525/");
                startActivity(intent);
                break;
            case R.id.tfiv5:
                intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", "https://www.qidian.com/rank/");
                startActivity(intent);
                break;
            case R.id.tfiv6:
                intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", "http://www.zongheng.com/");
                startActivity(intent);
                break;
            case R.id.tfiv7:
                intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", "http://www.jokeji.cn/");
                startActivity(intent);
                break;
            case R.id.tfiv8:
                intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", "http://www.yingshidaquan.cc/");
                startActivity(intent);
                break;
            case R.id.tfiv9:
                intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", "http://donghua.dmzj.com/");
                startActivity(intent);
                break;
            case R.id.tfiv10:
                intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", "http://www.530p.com/");
                startActivity(intent);
                break;
            case R.id.tfiv11:
                intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name", "http://www.92zw.la/");
                startActivity(intent);
                break;
        }
    }
}
