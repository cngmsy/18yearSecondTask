package com.example.shubowen.dragvideo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * 疏博文 新建于 2017/7/27.
 * 邮箱： shubw@icloud.com
 * 描述：请添加此文件的描述
 */

public class DragLayout extends FrameLayout {

    public static final String TAG = "DragLayout";

    private static final int INVALID_POINTER = -1;

    private final int mTouchSlopSquare;


    private boolean mIsBeingDragged;

    private float mInitialDownY;

    private float mInitialDownX;

    private float mCurrentX;
    private float mCurrentY;

    private OnDragListener mDragListener;

    public DragLayout(@NonNull Context context) {
        this(context, null);
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        final int touchSlop = configuration.getScaledTouchSlop();
        mTouchSlopSquare = touchSlop * touchSlop;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mIsBeingDragged = false;

                mInitialDownX = ev.getRawX();
                mInitialDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getRawX();
                final float y = ev.getRawY();
                startDragging(x, y);
                break;
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                break;
        }

        return mIsBeingDragged;
    }

    private void startDragging(float x, float y) {
        final float deltaX = x - mInitialDownX;
        final float deltaY = y - mInitialDownY;

        float distance = (deltaX * deltaX) + (deltaY * deltaY);

        if (!mIsBeingDragged && distance > mTouchSlopSquare) {
            mIsBeingDragged = true;
            mCurrentX = x;
            mCurrentY = y;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE: {

                final float x = ev.getRawX();
                final float y = ev.getRawY();

                startDragging(x, y);

                if (mIsBeingDragged) {
                    final float offsetX = x - mCurrentX;
                    final float offsetY = y - mCurrentY;

                    mCurrentX = x;
                    mCurrentY = y;

                    if ((Math.abs(offsetX) > 0 || Math.abs(offsetY) > 0) && null != mDragListener) {
                        mDragListener.onDrag(offsetX, offsetY);
                    } else {
                        return false;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                return false;
        }

        return true;
    }

    public void setDragListener(OnDragListener dragListener) {
        mDragListener = dragListener;
    }

    public interface OnDragListener {
        void onDrag(float scrollX, float scrollY);
    }

}
