package com.example.dawei.home_videoview;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.dawei.home_videoview.adapter.MyAdapter;
import com.example.dawei.home_videoview.fragment.AFragment;
import com.example.dawei.home_videoview.fragment.BFragment;
import com.example.dawei.home_videoview.fragment.CFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2018/1/16.
 */

public class HomeActivity extends AppCompatActivity {

    @InjectView(R.id.RadioGroup)
    android.widget.RadioGroup RadioGroup;
    @InjectView(R.id.ViewPager)
    android.support.v4.view.ViewPager ViewPager;
    private MyAdapter myAdapter;
    private AFragment aFragment;
    private BFragment bFragment;
    private CFragment cFragment;
    private ArrayList<Fragment> mList = new ArrayList<>();
    private FragmentManager Manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initView();
    }

    private void initView() {
        Manager = getSupportFragmentManager();
        aFragment = new AFragment();
        bFragment = new BFragment();
        cFragment = new CFragment();
        mList.add(aFragment);
        mList.add(bFragment);
        mList.add(cFragment);

        myAdapter = new MyAdapter(Manager, mList);
        ViewPager.setAdapter(myAdapter);

        RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.Btn_1:
                        ViewPager.setCurrentItem(0);
                        break;
                    case R.id.Btn_2:
                        ViewPager.setCurrentItem(1);
                        break;
                    case R.id.Btn_3:
                        ViewPager.setCurrentItem(2);
                        break;
                }
            }
        });

        ViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton childAt = (RadioButton) RadioGroup.getChildAt(position);
                childAt.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
