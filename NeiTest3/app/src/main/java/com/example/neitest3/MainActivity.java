package com.example.neitest3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.lv_memory)
    ListView lvMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        List<ProcessInfo> tempList = ProcessInfoProvider
                .getProcessInfo(this);
        MyAdapter myAdapter = new MyAdapter(MainActivity.this,tempList);
        lvMemory.setAdapter(myAdapter);
    }
}
