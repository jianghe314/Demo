package com.kangzhan.student.mUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kangzhan.student.R;

/**
 * Created by kangzhan011 on 2017/6/8.
 */

public class ShowStar extends LinearLayout {
    private ImageView star;
    private int starNum;
    private int starDis;
    private int starSize;

    public ShowStar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.ShowStar);
        starNum=array.getInt(R.styleable.ShowStar_StarNum,0);
        starDis= (int) array.getDimension(R.styleable.ShowStar_StarDistance,5);
        starSize= (int) array.getDimension(R.styleable.ShowStar_StarSize,10);
        array.recycle();
    }

}
