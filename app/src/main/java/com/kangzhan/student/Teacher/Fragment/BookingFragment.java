package com.kangzhan.student.Teacher.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.Adapter.TeachermainFragment;
import com.kangzhan.student.Teacher.Booking.BookingTrain1;
import com.kangzhan.student.Teacher.Booking.BookingTrain2;
import com.kangzhan.student.Teacher.Booking.BookingTrain3;
import com.kangzhan.student.com.BaseFragment;
import com.kangzhan.student.com.LazyFragment;
import com.kangzhan.student.mUI.mBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/4/7.
 */

public class BookingFragment extends Fragment implements View.OnClickListener{
    private TextView title1,title2,title3;
    private View line;
    private ViewPager viewPager;
    private int screenWidth;
    private int currentIndex;
    private mBanner banner;
    private List<Integer> imageUrl=new ArrayList<>();
    private ArrayList<Fragment> fragmentList=new ArrayList<>();
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view!=null){
            return view;
        }else {
            View view =inflater.inflate(R.layout.teacher_booking_layout,container,false);
            initView(view);
            initWidth();
            initViewPager();
            return view;
        }
    }

    private void initView(View v) {
        banner= (mBanner) v.findViewById(R.id.teacher_booking_banner);
        imageUrl.clear();
        imageUrl.add(R.drawable.banner1);
        imageUrl.add(R.drawable.banner2);
        imageUrl.add(R.drawable.banner3);
        imageUrl.add(R.drawable.banner4);
        banner.setBannerIntergerData(imageUrl);
        banner.startSmoothAuto();
        title1= (TextView) v.findViewById(R.id.techer_news_title1);
        title1.setOnClickListener(this);
        title2= (TextView) v.findViewById(R.id.techer_news_title2);
        title2.setOnClickListener(this);
        title3= (TextView) v.findViewById(R.id.techer_news_title3);
        title3.setOnClickListener(this);
        line=v.findViewById(R.id.teacher_booking_linespace);
        viewPager= (ViewPager) v. findViewById(R.id.teacher_booking_viewpager);
    }

    private void initWidth() {
        DisplayMetrics display=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(display);
        LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) line.getLayoutParams();
        screenWidth=display.widthPixels;
        lp.width=screenWidth/3;
        lp.leftMargin=(screenWidth/3);
        line.setLayoutParams(lp);
    }

    private void initViewPager() {
        fragmentList.add(new BookingTrain1());
        fragmentList.add(new BookingTrain2());
        fragmentList.add(new BookingTrain3());
        TeachermainFragment fragmentAdapter=new TeachermainFragment(getChildFragmentManager(),fragmentList);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(1);
        setTextColor();
        setChoiceTextColor(title2);
        //
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) line.getLayoutParams();
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - positionOffset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - positionOffset)
                            * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                }
                line.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                currentIndex=position;
                switch (position){
                    case 0:
                        setTextColor();
                        setChoiceTextColor(title1);
                        break;
                    case 1:
                        setTextColor();
                        setChoiceTextColor(title2);
                        break;
                    case 2:
                        setTextColor();
                        setChoiceTextColor(title3);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //恢复字体颜色
    private void setTextColor(){
        title1.setTextColor(getResources().getColor(R.color.textColor_gray));
        title2.setTextColor(getResources().getColor(R.color.textColor_gray));
        title3.setTextColor(getResources().getColor(R.color.textColor_gray));
    }



    //选中颜色
    private void setChoiceTextColor(TextView textView){
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.techer_news_title1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.techer_news_title2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.techer_news_title3:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.onResume();
    }
}
