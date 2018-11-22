package com.kangzhan.student.mUI;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/6/13.
 */
//Relative继承了ViewGroup
public class mBannerTest extends RelativeLayout implements View.OnClickListener{
    private Handler handler;
    private ViewPager mViewPager;
    private LinearLayout mPointerContainer;
    private int mCurrentPosition;
    //
    private ImageView mImageView;
    private List<ImageView> mImageList=new ArrayList<>();
    private List<String> mlist=new ArrayList<>();
    private ImageView mPoint;
    private int mPointDrawableId=R.drawable.selector_banner_point;

    private boolean auto = false;
    private int DEFAULT_INTERVAL=1500;
    private final int DEFAULT_WAIT_TIME=3000;
    private long mTouchTime;

    //初始化界面
    public mBannerTest(Context context) {
        this(context,null);
    }

    public mBannerTest(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public mBannerTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handler=new Handler(Looper.getMainLooper());
        initView();
    }

    private void initView() {
        mViewPager=new ViewPager(getContext());
        addView(mViewPager);
        //指示器容器
        mPointerContainer=new LinearLayout(getContext());
        mPointerContainer.setPadding(0,0,80,20);
        mPointerContainer.setGravity(Gravity.BOTTOM|Gravity.RIGHT);     //设置在右下角
        addView(mPointerContainer);
    }
    //初始化数据
    public mBannerTest setBannerData(ArrayList<String> list){
        mlist=list;
        if(list.size()>1){
            //多页情况
            for (int i = 0; i <list.size(); i++) {
                createNewView(list.get(i));
                mPoint.setImageResource(mPointDrawableId);
                mPoint.setEnabled(false);
                mPoint.setPadding(10,10,10,10);
                mPointerContainer.addView(mPoint);          //添加指示器
            }
            //设置循环播放
            // 第一页加在最后
            createNewView(list.get(0), -1);
            // 最后一页加在第一页
            createNewView(list.get(list.size() - 1), 0);
            mCurrentPosition=1;
            mCurrentPosition = 1;
            mViewPager.setOffscreenPageLimit(2);
            //mViewPager.addOnPageChangeListener(pageChangeListener);
        }else {
            //只有一页
            createNewView(list.get(0));
            mCurrentPosition = 0;
            mPoint = new ImageView(getContext());
            mPoint.setImageResource(mPointDrawableId);
        }
        mPoint.setEnabled(true);
        //ViewPager设置页面
        //mViewPager.setAdapter();
        mViewPager.setCurrentItem(mCurrentPosition);

        return this;
    }
    //对Banner生命周期的管理
    public mBannerTest onResume(){
        if(auto){
            handler.removeCallbacks(autoSmooth);
            handler.post(autoSmooth);                 //执行post里面的线程
        }
        return this;
    }

    public mBannerTest onPause(){
        handler.removeCallbacks(autoSmooth);
        handler.removeCallbacks(timeCount);
        return this;
    }

    //设置事件间隔
    public mBannerTest setSmoothInterval(int interval) {
        DEFAULT_INTERVAL = interval;
        return this;
    }


    /**
     * 自动滑动
     */
    public void startSmoothAuto() {
        if (mImageList.size() == 1) {
            return;
        }
        auto = true;
        handler.postDelayed(autoSmooth, DEFAULT_INTERVAL);
    }

    //页面滑动线程
    Runnable autoSmooth=new Runnable() {
        @Override
        public void run() {
            mCurrentPosition++;
            mCurrentPosition=mCurrentPosition%mlist.size();
            mViewPager.setCurrentItem(mCurrentPosition);
            handler.postDelayed(this,DEFAULT_INTERVAL);
        }
    };

    Runnable timeCount=new Runnable() {
        @Override
        public void run() {
            if(System.currentTimeMillis() - mTouchTime > DEFAULT_WAIT_TIME){
                handler.removeCallbacks(this);
                handler.removeCallbacks(autoSmooth);
                handler.post(autoSmooth);
                return;
            }
            handler.postDelayed(this,DEFAULT_INTERVAL);

        }
    };




    private void createNewView(String txt) {
        createNewView(txt,-1);
    }
    private void createNewView(String txt,int position){
        mImageView=new ImageView(getContext());
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(position==-1){
            mImageList.add(mImageView);
        }else {
            mImageList.add(position,mImageView);
        }
        //注册每一个页面的点击事件
        mImageView.setOnClickListener(this);
        //数据载入
        Glide.with(getContext()).load(txt).placeholder(R.drawable.banner).error(R.drawable.error).into(mImageView);

    }


    @Override
    public void onClick(View v) {

    }
}
