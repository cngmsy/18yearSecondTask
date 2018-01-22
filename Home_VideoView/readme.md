#视频引导页滑动（）
#效果图gif
![](ssaa.gif)



#实现的核心代码是
#步骤一:
#       设置播放视频的路径
#VideViews.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.a1));
        开始播放
        VideViews.start();


#步骤二:
#      要求视频循环播放： 
#VideViews.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                VideViews.start();
            }
        });



