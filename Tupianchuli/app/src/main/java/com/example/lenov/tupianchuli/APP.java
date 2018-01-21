package com.example.lenov.tupianchuli;

import android.app.Application;
import android.os.Handler;

/**
 * Created by lenov on 2018/1/17.
 */

public class APP extends Application {
    private static APP sInstance;
    public static APP getInstance() {
        return sInstance;
    }

    /**
     * 在主线程中刷新UI的方法
     *
     * @param r
     */
    public static void runOnUIThread(Runnable r) {
        APP.getMainHandler().post(r);
    }
    /**
     * app的入口函数
     */
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化context
        sInstance = this;
        //初始化handler
        mHandler = new Handler();
    }

    //qcl用来在主线程中刷新ui
    private static Handler mHandler;

    public static Handler getMainHandler() {
        return mHandler;
    }
}
