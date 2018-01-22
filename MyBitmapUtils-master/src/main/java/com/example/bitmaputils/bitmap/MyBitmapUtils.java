package com.example.bitmaputils.bitmap;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 自定义的bitmap工具类
 */
public class MyBitmapUtils {


    /**
     * 网络缓存
     */
    public NetCacheUtils mNetCacheUtils;

    /**
     * 本地缓存
     */
    public SDcardCacheUtils mSdCacheUtils;

    /**
     * 内存缓存
     */
    public MemoryCacheUtils mMemoryCacheUtils;


    public MyBitmapUtils() {
        mSdCacheUtils = new SDcardCacheUtils();
        mMemoryCacheUtils = new MemoryCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mSdCacheUtils, mMemoryCacheUtils);
    }

    /**
     * 展示图片的方法
     *
     * @param image
     * @param url
     */
    public void display(ImageView image, String url) {


        //从内存中读取
        Bitmap fromMemroy = mMemoryCacheUtils.getFromMemroy(url);
        //如果内存中有的h话就直接返回，从内存中读取
        if (fromMemroy != null) {
            image.setImageBitmap(fromMemroy);

            return;
        }


        //从本地SD卡读取
        Bitmap fromSd = mSdCacheUtils.getFromSd(url);
        if (fromSd != null) {
            image.setImageBitmap(fromSd);

            mMemoryCacheUtils.setToMemory(url, fromSd);

            return;
        }
        //从网络中读取
        mNetCacheUtils.getDataFromNet(image, url);

    }
}
