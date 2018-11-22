package com.kangzhan.student.mUI;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/6/13.
 */

public class mBanner extends FrameLayout implements View.OnClickListener {

    private ViewPager mViewPager;
    private ImageView mImageView;
    private List<ImageView> mImageList = new ArrayList<>();


    private int mCurrentPosition;
    private Handler mHandler;
    private int DEFAULT_INTERVAL = 2500;
    private int DEFAULT_WAIT_TIME = 3000; // 3s 后用户没有点击,继续自动滑动
    private long mTouchTime;

    private boolean auto = false;
    private OnItemClickListener mOnItemClickListener;

    //private LinearLayout mPointContainer; // 存放点的容器
    private int mPointDrawableId = R.drawable.selector_banner_point; // 点的drawable资源id
    private ImageView mPoint;

    public mBanner(Context context) {
        this(context, null);
    }

    public mBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public mBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHandler = new Handler(Looper.getMainLooper());
        initView();
    }

    public mBanner setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
        return this;
    }

    /**
     * 设置数据,初始化view部分
     *
     * @param list imageUri 使用Glide 加载
     * @return
     */
    public mBanner setBannerURLData(List<String> list) {

        if (list.size() > 1) {
            for (int i = 0; i < list.size(); i++) {
//            mImageView = new ImageView(getContext());
                createNewView(list.get(i));
                // point
                mPoint = new ImageView(getContext());
                mPoint.setImageResource(mPointDrawableId);
                mPoint.setEnabled(false);
                mPoint.setPadding(10, 0, 10, 0);
                //mPointContainer.addView(mPoint);
            }
            // 第一页加在最后
            createNewView(list.get(0), -1);
            // 最后一页加在第一页
            createNewView(list.get(list.size() - 1), 0);

            mCurrentPosition = 1;
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.addOnPageChangeListener(pageChangeListener);

        } else {
            createNewView(list.get(0));
            mCurrentPosition = 0;

            mPoint = new ImageView(getContext());
            mPoint.setImageResource(mPointDrawableId);
        }
        mPoint.setEnabled(true);
//        mPointContainer.addView(mPoint);
        //
        mViewPager.setAdapter(new InnerPagerAdapter());
        mViewPager.setCurrentItem(mCurrentPosition);

        return this;
    }

    public void clearBannerData(){
        mImageList.clear();
        new InnerPagerAdapter().notifyDataSetChanged();
    }

    public mBanner setBannerIntergerData(List<Integer> list) {

        if (list.size() > 1) {
            //mPointContainer.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
//            mImageView = new ImageView(getContext());
                createNewView(list.get(i));
                // point
                mPoint = new ImageView(getContext());
                mPoint.setImageResource(mPointDrawableId);
                mPoint.setEnabled(false);
                mPoint.setPadding(10, 0, 10, 0);
                //mPointContainer.addView(mPoint);
            }
            // 第一页加在最后
            createNewView(list.get(0), -1);
            // 最后一页加在第一页
            createNewView(list.get(list.size() - 1), 0);

            mCurrentPosition = 1;
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.addOnPageChangeListener(pageChangeListener);

        } else {
            createNewView(list.get(0));
            mCurrentPosition = 0;

            mPoint = new ImageView(getContext());
            mPoint.setImageResource(mPointDrawableId);
        }
        mPoint.setEnabled(true);
//        mPointContainer.addView(mPoint);
        //
        mViewPager.setAdapter(new InnerPagerAdapter());
        mViewPager.setCurrentItem(mCurrentPosition);

        return this;
    }



       //* 对应activity的生命周期

    public mBanner onResume() {
        if (auto) {
            mHandler.removeCallbacks(autoSmooth);
            mHandler.post(autoSmooth);
        }
        return this;
    }

    public mBanner onPause() {
        mHandler.removeCallbacks(autoSmooth);
        mHandler.removeCallbacks(timeCount);
        return this;
    }

    private void createNewView(String text) {
        createNewView(text, -1);
    }


    private void createNewView(Integer text) {
        createNewView(text, -1);
    }

    private void createNewView(String url, int position ) {
        mImageView = new ImageView(getContext());
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (position == -1) {
            mImageList.add(mImageView);
        } else {
            mImageList.add(position, mImageView);
        }
        mImageView.setOnClickListener(this);

        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.kzplaceholder)
                .error(R.drawable.kzplaceholder)
                .into(mImageView);
    }


    private void createNewView(Integer url, int position) {
        mImageView = new ImageView(getContext());
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (position == -1) {
            mImageList.add(mImageView);
        } else {
            mImageList.add(position, mImageView);
        }
        mImageView.setOnClickListener(this);

        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.kzplaceholder)
                .error(R.drawable.kzplaceholder)
                .into(mImageView);
    }


    /**
     * 时间间隔
     *
     * @param interval
     */
    public mBanner setSmoothInterval(int interval) {
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
        mHandler.postDelayed(autoSmooth, DEFAULT_INTERVAL);
    }

    Runnable autoSmooth = new Runnable() {
        @Override
        public void run() {
            mCurrentPosition++;
            mCurrentPosition = mCurrentPosition % mImageList.size();
            mViewPager.setCurrentItem(mCurrentPosition);
            mHandler.postDelayed(this, DEFAULT_INTERVAL);
        }
    };
    Runnable timeCount = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - mTouchTime > DEFAULT_WAIT_TIME) {
                mHandler.removeCallbacks(this);
                mHandler.removeCallbacks(autoSmooth);
                mHandler.post(autoSmooth);
                return;
            }
            mHandler.postDelayed(this, DEFAULT_INTERVAL);
        }
    };

    private void initView() {
        mViewPager = new ViewPager(getContext());
        addView(mViewPager);

        //mPointContainer = new LinearLayout(getContext());
        //mPointContainer.setPadding(0, 0, 80, 20);
        //mPointContainer.setGravity(Gravity.BOTTOM | Gravity.RIGHT);

        //addView(mPointContainer);
    }
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.i("slack","onPageScrolled...");
        }

        @Override
        public void onPageSelected(int position) {
//            Log.i("slack","onPageSelected...");
            mCurrentPosition = position;
            switchToPoint();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            if (state == ViewPager.SCROLL_STATE_IDLE && mImageList.size() > 1) {
                if (mCurrentPosition == 0) {
                    mCurrentPosition = mImageList.size() - 2;
                    mViewPager.setCurrentItem(mCurrentPosition, false);
                } else if (mCurrentPosition == (mImageList.size() - 1)) {
                    mCurrentPosition = 1;
                    mViewPager.setCurrentItem(mCurrentPosition, false);
                }
//                Log.i("slack","onPageScrollStateChanged..." + mCurrentPosition);
            }

        }
    };

    /*
      * point 假如有三个实际页面,0-2
            */
    private void switchToPoint() {

        if (mCurrentPosition == 0 || mCurrentPosition == mImageList.size() - 1) {
            return;
        }

       /* for (int i = 0; i < mPointContainer.getChildCount(); i++) {
            mPointContainer.getChildAt(i).setEnabled(false);
        }

        mPointContainer.getChildAt(mCurrentPosition - 1).setEnabled(true);*/
    }

    @Override
    public void onClick(View view) {
//        Log.i("slack", "pos: " + mCurrentPosition);
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(mCurrentPosition - 1);
        }
    }

    /**
     * viewPager的适配器
     */
    private class InnerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageList.size() > 1 ? mImageList.size() : 1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(mImageList.get(position));
            return mImageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    // ACTION_UP 滑动冲突,获取不到  
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.i("slack","onInterceptTouchEvent..." + ev.toString());  
        if (auto) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                mHandler.removeCallbacks(autoSmooth);
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                mTouchTime = System.currentTimeMillis();
                mHandler.postDelayed(timeCount, 100);
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }



}
