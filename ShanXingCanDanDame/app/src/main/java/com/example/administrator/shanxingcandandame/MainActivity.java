package com.example.administrator.shanxingcandandame;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

public class MainActivity extends BaseActivity implements IActivitv {
    private TextView tv, tv1, tv2, tv3, tv4, tv5;
    private int tvx, tvy;
    private int ZHENG = 200;
    private int XIE = 141;
    private float FZHENGA = -200.0f;
    private float FXIEA = -141.0f;
    private float ZZHENGA = 200.0f;
    private float ZXIEA = 141.0f;
    // 菜单开闭状态
    private Boolean dFlag = true;
    private int winx;
    private int winy;
    private int statusHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initOpers();
        initDates();
        menu();
    }

    @Override
    public void initViews() {
        tv = (TextView) findViewById(R.id.menu_caidan);
        tv1 = (TextView) findViewById(R.id.menu_caidan1);
        tv2 = (TextView) findViewById(R.id.menu_caidan2);
        tv3 = (TextView) findViewById(R.id.menu_caidan3);
        tv4 = (TextView) findViewById(R.id.menu_caidan4);
        tv5 = (TextView) findViewById(R.id.menu_caidan5);
    }

    @Override
    public void initOpers() {

    }

    @Override
    public void initDates() {
        // 获取通知栏高度
        statusHeight = getStatusBarHeight(this);
    }

    public void click(View v) {

        // 获取按钮位置
        int[] location1 = new int[2];
        tv.getLocationOnScreen(location1);
        tvx = location1[0];
        // 这里需要减去通知栏高度
        tvy = location1[1] - statusHeight;// 38/55;
        // 点击事件
        switch (v.getId()) {
            case R.id.menu_caidan:
                if (dFlag) {
                    openAnimation();
                    dFlag = false;
                } else {
                    closed();
                    dFlag = true;
                }
                break;
            case R.id.menu_caidan1:
                toasts("菜单1");
                break;
            case R.id.menu_caidan2:
                toasts("菜单2");
                break;
            case R.id.menu_caidan3:
                toasts("菜单3");
                break;
            case R.id.menu_caidan4:
                toasts("菜单4");
                break;
            case R.id.menu_caidan5:
                toasts("菜单5");
                break;
        }
    }

    // 界面菜单移动事件处理
    public void yidong(View v, int x, int y) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(v.getLayoutParams());
        margin.setMargins(x, y, x + tv.getWidth(), y + tv.getHeight());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                margin);
        v.setLayoutParams(layoutParams);

    }

    // 菜单事件管理
    public void menu() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        winx = dm.widthPixels;
        winy = dm.heightPixels;
        int[] location = new int[2];
        tv.getLocationOnScreen(location);
        tvx = location[0];
        tvy = location[1];
        tv.setOnTouchListener(new MyListener());
    }

    // tv屏幕操作监听
    public class MyListener implements View.OnTouchListener {
        private float x;
        private float y;
        private int tvx;
        private Boolean tFlag = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    tFlag = true;
                    x = event.getRawX();
                    y = event.getRawY() - 55;
                    yidong(v, (int) x, (int) y);
                    yidong(tv1, (int) x, (int) y);
                    yidong(tv2, (int) x, (int) y);
                    yidong(tv3, (int) x, (int) y);
                    yidong(tv4, (int) x, (int) y);
                    yidong(tv5, (int) x, (int) y);
                    break;
                case MotionEvent.ACTION_UP:
                    // tFlag避免长按事件未执行时(x,y值为0)移动到0位置
                    if (tFlag) {
                        tFlag = false;
                        // 松手时判断当前位置
                        // 保证菜单在左边或右边
                        // 保证打开时不会出现在屏幕外
                        int[] location = new int[2];
                        tv.getLocationOnScreen(location);
                        tvx = location[0];
                        tvy = location[1];
                        // 上方位置限制
                        if (y < 200) {
                            y = 200;
                        }
                        // 下方位置限制
                        if (tvy > winy - 200 - tv.getHeight()) {
                            y = winy - tv.getHeight() - 200 - statusHeight;
                        }
                        // 如果x位置大于屏幕宽的一半最终位置在屏幕右边
                        if (x > (winx / 2 + tv.getWidth() / 2)) {
                            x = winx - (tv.getWidth());
                        } else {
                            x = 0;
                        }
                        // 确定最终位置
                        yidong(v, (int) x, (int) y);
                        yidong(tv1, (int) x, (int) y);
                        yidong(tv2, (int) x, (int) y);
                        yidong(tv3, (int) x, (int) y);
                        yidong(tv4, (int) x, (int) y);
                        yidong(tv5, (int) x, (int) y);
                    }
                    break;

            }
            return false;
        }
    }

    // 获取通知栏高度
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    // 打开菜单操作
    private void open() {
        // 判断菜单位置左边和右边打开方向不一样
        if (tvx > 300) {
            yidong(tv1, tvx, tvy - ZHENG);
            yidong(tv2, tvx - XIE, tvy - XIE);
            yidong(tv3, tvx - ZHENG, tvy);
            yidong(tv4, tvx - XIE, tvy + XIE);
            yidong(tv5, tvx, tvy + ZHENG);
        } else {
            yidong(tv1, tvx, tvy - ZHENG);
            yidong(tv2, tvx + XIE, tvy - XIE);
            yidong(tv3, tvx + ZHENG, tvy);
            yidong(tv4, tvx + XIE, tvy + XIE);
            yidong(tv5, tvx, tvy + ZHENG);
        }
    }

    // 关闭菜单操作
    private void closed() {
        yidong(tv1, tvx, tvy);
        yidong(tv2, tvx, tvy);
        yidong(tv3, tvx, tvy);
        yidong(tv4, tvx, tvy);
        yidong(tv5, tvx, tvy);
    }

    // ------------------------------------------------------------------------------
    // 开关动画

    /**
     * 这里可以去掉   去掉后没有动画菜单打开方式为闪现
     */
    @SuppressWarnings("unused")
    private void openAnimation() {
        if (tvx > 200) {
            donghua(tv1, 0, FZHENGA);
            donghua(tv2, FXIEA, FXIEA);
            donghua(tv3, FZHENGA, 0);
            donghua(tv4, FXIEA, ZXIEA);
            donghua(tv5, 0, ZZHENGA);

        } else {
            donghua(tv1, 0, FZHENGA);
            donghua(tv2, ZXIEA, FXIEA);
            donghua(tv3, ZZHENGA, 0);
            donghua(tv4, ZXIEA, ZXIEA);
            donghua(tv5, 0, ZZHENGA);
        }
    }

    private void closedAnimation() {
        tv1.clearAnimation();
        tv2.clearAnimation();
        tv3.clearAnimation();
        tv4.clearAnimation();
        tv5.clearAnimation();

    }

    public void donghua(View v, float x, float y) {
        Animation mTranslateAnimation = new TranslateAnimation(0, x, 0, y);// 移动
        //动画执行时间
        mTranslateAnimation.setDuration(500);
        AnimationSet mAnimationSet = new AnimationSet(false);
        mAnimationSet.setFillAfter(true);
        mAnimationSet.addAnimation(mTranslateAnimation);
        v.startAnimation(mAnimationSet);
        //只需执行1次
        int flagone = 1;
        if (flagone == 1) {
            flagone = 0;

            mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    closedAnimation();
                    open();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

}
