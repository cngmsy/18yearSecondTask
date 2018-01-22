package com.jiyun.neicunyouhua;
/**
 * 解决方案
 * 将非静态匿名内部类修改为静态匿名内部类
 * */

import android.app.Activity;
import android.os.Bundle;

public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        test();
    }
    //加上static，变成静态匿名内部类
    public static void test() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

