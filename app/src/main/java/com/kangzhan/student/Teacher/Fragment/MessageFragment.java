package com.kangzhan.student.Teacher.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.Adapter.TeachermainFragment;
import com.kangzhan.student.Teacher.News.AdviseFragment;
import com.kangzhan.student.Teacher.News.CheckNoticeFragment;
import com.kangzhan.student.Teacher.News.SendMsgFragment;
import com.kangzhan.student.Teacher.News.SendNoticeFragment;
import com.kangzhan.student.com.BaseFragment;
import com.kangzhan.student.com.LazyFragment;

import java.util.ArrayList;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.offset;

/**
 * Created by kangzhan011 on 2017/4/7.
 */

public class MessageFragment extends Fragment implements View.OnClickListener{
    private View view;
    private TextView titile1,titile2,titile3,titile4;
    private ViewPager viewPager;
    private ImageView linespace;
    private int screenWidth;
    private int currentIndex;
    private int gg;
    private ArrayList<Fragment> fragmentList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.teacher_msg_layout,container,false);
            initView(view);
            initWidth();
            initViewPager();
        }
        return view;
    }


    private void initView(View v) {
        linespace= (ImageView) v.findViewById(R.id.teacher_news_linespace);
        titile1= (TextView) v.findViewById(R.id.techer_news_title1);
        titile1.setOnClickListener(this);
        titile2= (TextView) v.findViewById(R.id.techer_news_title2);
        titile2.setOnClickListener(this);
        titile3= (TextView) v.findViewById(R.id.techer_news_title3);
        titile3.setOnClickListener(this);
        titile4= (TextView) v.findViewById(R.id.techer_news_title4);
        titile4.setOnClickListener(this);
        viewPager= (ViewPager) v.findViewById(R.id.teacher_news_viewpager);
    }


    private void initWidth() {
        //这两个方法都能获取屏幕宽度
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        //获取控件的布局参数
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) linespace.getLayoutParams();
        gg=screenWidth/16;
        lp.width=screenWidth/4;
        linespace.setLayoutParams(lp);
    }
    private void initViewPager() {
        fragmentList.add(new CheckNoticeFragment());
        fragmentList.add(new AdviseFragment());
        fragmentList.add(new SendNoticeFragment());
        fragmentList.add(new SendMsgFragment());
        TeachermainFragment adapter=new TeachermainFragment(getChildFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        setTextColor();
        setChoiceTextColor(titile1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //postion:位置  positionOffset:位置偏移  positionOffsetPixels：位置偏移像素
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) linespace.getLayoutParams();
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 4) + currentIndex
                            * (screenWidth / 4));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - positionOffset)
                            * (screenWidth * 1.0 / 4) + currentIndex
                            * (screenWidth / 4));

                } else if (currentIndex == 1 && position == 1) // 1->2
                {
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 4) + currentIndex
                            * (screenWidth / 4));
                } else if (currentIndex == 2 && position == 1) // 2->1
                {
                    lp.leftMargin = (int) (-(1 - positionOffset)
                            * (screenWidth * 1.0 / 4) + currentIndex
                            * (screenWidth / 4));
                }
                else if(currentIndex==2&&position==2){         //2>3
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 4) + currentIndex
                            * (screenWidth / 4));
                }else if(currentIndex==3&&position==2){                      //3>2
                    lp.leftMargin = (int) (-(1 - positionOffset)
                            * (screenWidth * 1.0 / 4) + currentIndex
                            * (screenWidth / 4));
                }
                linespace.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                currentIndex=position;
                switch (position){
                    case 0:
                        setTextColor();
                        setChoiceTextColor(titile1);
                        break;
                    case 1:
                        setTextColor();
                        setChoiceTextColor(titile2);
                        break;
                    case 2:
                        setTextColor();
                        setChoiceTextColor(titile3);
                        break;
                    case 3:
                        setTextColor();
                        setChoiceTextColor(titile4);
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
            case R.id.techer_news_title4:
                viewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }


    //恢复字体颜色
    private void setTextColor(){
        titile1.setTextColor(getResources().getColor(R.color.textColor_gray));
        titile2.setTextColor(getResources().getColor(R.color.textColor_gray));
        titile3.setTextColor(getResources().getColor(R.color.textColor_gray));
        titile4.setTextColor(getResources().getColor(R.color.textColor_gray));
    }



    //选中颜色
    private void setChoiceTextColor(TextView textView){
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}
