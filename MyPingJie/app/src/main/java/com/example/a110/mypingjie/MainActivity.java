package com.example.a110.mypingjie;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Panel view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap bit1 = BitmapFactory.decodeStream(getApplicationContext()
                .getResources().openRawResource(R.drawable.shang));
        Bitmap bit2 = BitmapFactory.decodeStream(getApplicationContext()
                .getResources().openRawResource(R.drawable.xia));
        view = new Panel(this, bit1, bit2);
        setContentView(view);

    }
    }

