package com.kangzhan.student.mUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kangzhan.student.R;

/**
 * Created by kangzhan011 on 2017/11/17.
 */

public class RemarkStar extends LinearLayout {
    private int size;
    private int sum;
    private int div;
    private int hasStar=0;
    private Paint paint;


    public RemarkStar(Context context) {
        this(context,null);
    }

    public RemarkStar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RemarkStar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(HORIZONTAL);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.RemarkStar);
        //尺寸
        size=array.getDimensionPixelSize(R.styleable.RemarkStar_RemarkStar_Size,15);
        //默认多少个，没有颜色
        sum=array.getInteger(R.styleable.RemarkStar_RemarkStar_Sum,5);
        //间距
        div=array.getDimensionPixelSize(R.styleable.RemarkStar_RemarkStar_Div,5);
        array.recycle();

        paint=new Paint();
        paint.setAntiAlias(true);
    }

    private void loadStar() {
        removeAllViews();
        LayoutParams params=new LayoutParams(size,size);
        params.weight=1;
        params.setMargins(div,0,div,0);
        if(hasStar>0){
            for (int i = 0; i <hasStar ; i++) {
                ImageView iv=new ImageView(getContext());
                iv.setImageResource(R.drawable.star_1);
                addView(iv,params);
            }
            for (int i = 0; i <sum-hasStar ; i++) {
                ImageView iv=new ImageView(getContext());
                iv.setImageResource(R.drawable.star_0);
                addView(iv,params);
            }
        }else {
            for (int i = 0; i <sum-hasStar ; i++) {
                ImageView iv=new ImageView(getContext());
                iv.setImageResource(R.drawable.star_0);
                addView(iv,params);
            }
        }
        LayoutParams params1=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(div,0,div,0);
        TextView tv=new TextView(getContext());
        tv.setTextSize(12);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv.setText(hasStar+".0");
        addView(tv,params1);

    }

    public void setRemarkStar(int starSum){
        hasStar=starSum;
        loadStar();
    }

}
