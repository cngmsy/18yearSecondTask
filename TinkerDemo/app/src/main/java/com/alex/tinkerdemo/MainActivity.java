package com.alex.tinkerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.tinkerdemo.tinker.util.Utils;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView xianshi;
    private TextView jisuan;
    private TextView xiufu;
    private TextView gengxin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 加载补丁包

        initView();


        // 清除补丁包
        //Tinker.with(getApplicationContext()).cleanPatch();
    }

    /**
     * =====================Tinker 默认配置========================
     */

    protected void onResume() {
        super.onResume();
        Utils.setBackground(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.setBackground(true);
    }

    private void initView() {
        xianshi = (TextView) findViewById(R.id.xianshi);
        jisuan = (TextView) findViewById(R.id.jisuan);
        jisuan.setOnClickListener(this);
        xiufu = (TextView) findViewById(R.id.xiufu);
        xiufu.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch ((view.getId())) {
            case R.id.jisuan:
                JiSuan jiSuan = new JiSuan();
                int result = jiSuan.result();
                xianshi.setText(result + "");
                Toast.makeText(MainActivity.this, "点击了计算按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.xiufu:
                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), "/sdcard/buding.apk");
                Toast.makeText(MainActivity.this, "点击了修复按钮", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
