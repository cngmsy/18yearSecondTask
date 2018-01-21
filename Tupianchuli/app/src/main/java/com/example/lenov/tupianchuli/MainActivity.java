package com.example.lenov.tupianchuli;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView image;
    EditText edit;
    private ImageView image2;
    private Button button2;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        image = (ImageView) findViewById(R.id.image);
        edit = (EditText) findViewById(R.id.edit);


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pattern = edit.getText().toString();
                int scaleRatio = 0;
                if (TextUtils.isEmpty(pattern)) {
                    scaleRatio = 0;
                } else if (scaleRatio < 0) {
                    scaleRatio = 10;
                } else {
                    scaleRatio = Integer.parseInt(pattern);
                }

                //        获取需要被模糊的原图bitmap
                Resources res = getResources();
                Bitmap scaledBitmap = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher_round);

                //        scaledBitmap为目标图像，10是缩放的倍数（越大模糊效果越高）
                Bitmap blurBitmap = FastBlurUtil.toBlur(scaledBitmap, scaleRatio);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setImageBitmap(blurBitmap);
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //url为网络图片的url，10 是缩放的倍数（越大模糊效果越高）
                final String pattern = edit.getText().toString();

                final String url =
                        "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1516071441&di=bd9f31baa98cd9d693151f7e23517a1f&src=http://image.tianjimedia.com/uploadImages/2015/209/42/6H182F668EP9.jpg";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int scaleRatio = 0;
                        if (TextUtils.isEmpty(pattern)) {
                            scaleRatio = 0;
                        } else if (scaleRatio < 0) {
                            scaleRatio = 10;
                        } else {
                            scaleRatio = Integer.parseInt(pattern);
                        }
                        //                        下面的这个方法必须在子线程中执行
                        final Bitmap blurBitmap2 = FastBlurUtil.GetUrlBitmap(url, scaleRatio);

                        //                        刷新ui必须在主线程中执行
                        APP.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                image.setImageBitmap(blurBitmap2);
                            }
                        });
                    }
                }).start();


            }
        });
    }

    private void initView() {

        image2 = (ImageView) findViewById(R.id.image2);
        button2 = (Button) findViewById(R.id.button2);
        button = (Button) findViewById(R.id.button);

    }


}
