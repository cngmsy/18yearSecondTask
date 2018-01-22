package com.example.a110.mypingjie;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by 110 on 2018/1/16.
 */

public class Panel extends View {


    private Bitmap bitmap;
    public Panel(Context context,Bitmap bit1,Bitmap bit2) {
        super(context);
        bitmap = newBitmap(bit1,bit2);
    }
    /**
     * 拼接图片
     * @param bit1
     * @param bit2
     * @return 返回拼接后的Bitmap
     */
    private Bitmap newBitmap(Bitmap bit1,Bitmap bit2){

        int width = bit1.getWidth();
        int height = bit1.getHeight() + bit2.getHeight();
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bit1, 0, 0, null);
        canvas.drawBitmap(bit2, 0, bit1.getHeight(), null);
        return bitmap;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
        bitmap.recycle();
    }



}

