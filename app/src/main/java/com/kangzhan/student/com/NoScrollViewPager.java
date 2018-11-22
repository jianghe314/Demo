package com.kangzhan.student.com;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by kangzhan011 on 2017/11/20.
 */

public class NoScrollViewPager extends ViewPager {
    private boolean isScroll=true;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.isScroll&&super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.isScroll&&super.onInterceptTouchEvent(ev);
    }
    /*
    public void setIsScrollEnable(boolean b){
        this.isScroll=b;
    }
    */
}
