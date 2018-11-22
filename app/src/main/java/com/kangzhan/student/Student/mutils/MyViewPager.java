package com.kangzhan.student.Student.mutils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by kangzhan011 on 2017/3/30.
 */

public class MyViewPager extends ViewPager {

    private float x;
    private float mLastMotionX;
    private boolean isComplete = false;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void isCompleteable(boolean iscomplete) {
        this.isComplete = iscomplete;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return super.onTouchEvent(arg0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX(); // 在手指按下的时获取X轴的坐标
                break;
            case MotionEvent.ACTION_MOVE:
                mLastMotionX = ev.getX() - x; // 滑动的距离
                if (!isComplete) {
                    if (mLastMotionX < 0) {
                        return false;  //不可以滑动
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return super.onInterceptTouchEvent(arg0);
    }

}
