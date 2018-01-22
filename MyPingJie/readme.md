#图片拼接
效果图gif
![](pj.jpg)



##步骤一:

```
/**
  * 创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
  */

         int width = bit1.getWidth();
        int height = bit1.getHeight() + bit2.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
```


##步骤二:

```
/**
 *将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
 */


        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bit1, 0, 0, null);
        canvas.drawBitmap(bit2, 0, bit1.getHeight(), null);



```
