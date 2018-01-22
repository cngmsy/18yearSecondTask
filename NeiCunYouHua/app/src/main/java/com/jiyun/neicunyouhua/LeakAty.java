package com.jiyun.neicunyouhua;
//Handler引起的内存泄漏
//mHandler 为匿名内部类实例，会引用外围对象LeakAty.this,
// 如果该Handler在Activity退出时依然还有消息需要处理，那么这个Activity就不会被回收。

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class LeakAty extends Activity {


    public TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_aty);
        fetchData();

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    // 刷新数据
                    break;
                default:
                    break;
            }

        };
    };

    private void fetchData() {
        //获取数据
        mHandler.sendEmptyMessage(0);
    }
}
