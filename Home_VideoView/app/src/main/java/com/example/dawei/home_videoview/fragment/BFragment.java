package com.example.dawei.home_videoview.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dawei.home_videoview.CustomVideoView;
import com.example.dawei.home_videoview.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2018/1/16.
 */

public class BFragment extends Fragment {

    @InjectView(R.id.VideoViewVB)
    CustomVideoView VideViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, null);
        ButterKnife.inject(this, view);

//        设置播放视频的路径
        VideViews.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.a2));
        //开始播放
        VideViews.start();
        //循环播放
        VideViews.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                VideViews.start();
            }
        });
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
