package com.example.bitmaputils.bitmap;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * 内存缓存
 */
public class MemoryCacheUtils {

    /**
     * LinkedHashMap<>(10,0.75f,true);
     * <p/>
     * 10是最大致   0.75f是加载因子   true是访问排序   false插入排序
     *
     *
     */
    //private LinkedHashMap<String,Bitmap> mMemoryCache = new LinkedHashMap<>(5,0.75f,true);

    private LruCache<String, Bitmap> mLruCache;


    public MemoryCacheUtils() {
        long maxMemory = Runtime.getRuntime().maxMemory();//最大内存  默认是16兆  运行时候的
        mLruCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //int byteCount = value.getByteCount();
                //得到图片字节数
                // @return number of bytes between rows of the native bitmap pixels.
                int byteCount = value.getRowBytes() * value.getWidth();
                return byteCount;
            }
        };
    }

    /**
     * 从内存中读取
     *
     * @param url
     */
    public Bitmap getFromMemroy(String url) {

        Log.d("MyBitmapUtils", "从内存中加载图片");
        return mLruCache.get(url);
    }

    /**
     * 写入到内存中
     *
     * @param url
     * @param bitmap
     */
    public void setToMemory(String url, Bitmap bitmap) {
        mLruCache.put(url, bitmap);
    }
}
