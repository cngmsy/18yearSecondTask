package com.jiyun.neicunyouhua;
//Handle解决方案

/**
 * 资源未关闭引起的内存泄漏
 * 当使用了BraodcastReceiver、Cursor、Bitmap等资源时，
 * 当不需要使用时，需要及时释放掉，若没有释放，则会引起内存泄漏
 *
 * 就是资源在不需要的时候没有被释放掉。所以在编码的过程中要注意这些细节，提高程序的性能。
 */
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class LeakAty1 extends Activity {
    private TextView tvResult1;
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_aty1);
        tvResult1 = (TextView) findViewById(R.id.tvResult);
        handler = new MyHandler(this);
        fetchData();

    }
    //第一步，将Handler改成静态内部类。
    private static class MyHandler extends Handler {
        //第二步，将需要引用Activity的地方，改成弱引用。
        private WeakReference<LeakAty> atyInstance;
        public MyHandler(LeakAty aty) {
            this.atyInstance = new WeakReference<LeakAty>(aty);
        }

        public MyHandler(LeakAty1 leakAty1) {
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LeakAty aty = atyInstance == null ? null : atyInstance.get();
            //如果Activity被释放回收了，则不处理这些消息
            if (aty == null||aty.isFinishing()) {
                return;
            }
//            aty.tvResult.setText("fetch data success");
        }
    }

    private void fetchData() {
        // 获取数据
        handler.sendEmptyMessage(0);
    }

    @Override
    protected void onDestroy() {
        //第三步，在Activity退出的时候移除回调
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
