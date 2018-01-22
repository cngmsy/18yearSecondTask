package com.jiyun.neicunyouhua;

import android.content.Context;

/**
 * Created by Administrator on 2018/1/21.
 * 单例引起的Context内存泄漏
 */
public class IMManager {
    private Context context;
    private static IMManager mInstance;

    public static IMManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (IMManager.class) {
                if (mInstance == null)
                    mInstance = new IMManager(context);
            }
        }
        return mInstance;
    }

    private IMManager(Context context) {
        this.context = context;
    }

}