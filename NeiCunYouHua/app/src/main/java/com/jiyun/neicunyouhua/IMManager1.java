package com.jiyun.neicunyouhua;

import android.content.Context;

/**
 * Created by Administrator on 2018/1/21.
 * 解决方案
 * 传入Application的context,因为Application的context的生命周期比Activity长，
 * 可以理解为Application的context与单例的生命周期一样长，传入它是最合适的。
 */

public class IMManager1 {
    private Context context;
    private static IMManager1 mInstance;

    public static IMManager1 getInstance(Context context) {
        if (mInstance == null) {
            synchronized (IMManager.class) {
                if (mInstance == null)
                    //将传入的context转换成Application的context
                    mInstance = new IMManager1(context.getApplicationContext());
            }
        }
        return mInstance;
    }

    private IMManager1(Context context) {
        this.context = context;
    }

}
