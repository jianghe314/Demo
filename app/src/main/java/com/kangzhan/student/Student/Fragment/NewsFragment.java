package com.kangzhan.student.Student.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.mainFragment;
import com.kangzhan.student.Student.News.StudentAdviseFragment;
import com.kangzhan.student.Student.News.StudentNewsNoticeFragment;
import com.kangzhan.student.com.BaseFragment;
import com.kangzhan.student.com.LazyFragment;

import java.util.ArrayList;

/**
 * Created by zhengxuan on 2017/3/27.
 */

public class NewsFragment extends Fragment implements View.OnClickListener {
    private View view ;
    private TextView notice,advise;
    private View lineSpace;
    private ViewPager viewPager;
    private int screenWidth,currentIndex;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.activity_news,container,false);
            initView(view);
        }
        return view;
    }

    private void initView(View v) {
        notice= (TextView) v.findViewById(R.id.student_news_title1);
        notice.setOnClickListener(this);
        advise= (TextView) v.findViewById(R.id.student_news_title2);
        advise.setOnClickListener(this);
        lineSpace=v.findViewById(R.id.student_news_linespace);
        viewPager= (ViewPager) v.findViewById(R.id.student_news_viewPager);
        initWidth();
        initViewPager();
    }


    private void initWidth() {
        //这两个方法都能获取屏幕宽度
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        //获取控件的布局参数
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lineSpace.getLayoutParams();

        lp.width=screenWidth/2;
        lineSpace.setLayoutParams(lp);
    }
    private void initViewPager() {
        ArrayList<Fragment> fragmentsList=new ArrayList<>();
        fragmentsList.add(new StudentNewsNoticeFragment());
        fragmentsList.add(new StudentAdviseFragment());
        mainFragment fragmentAdapter=new mainFragment(getChildFragmentManager(),fragmentsList);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0);
        setTextColor();
        setChoiceTextColor(notice);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) lineSpace.getLayoutParams();
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - positionOffset)
                            * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2));
                }
                lineSpace.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                currentIndex=position;
                switch (position){
                    case 0:
                        setTextColor();
                        setChoiceTextColor(notice);
                        break;
                    case 1:
                        setTextColor();
                        setChoiceTextColor(advise);
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
        notice.setTextColor(getResources().getColor(R.color.textColor_gray));
        advise.setTextColor(getResources().getColor(R.color.textColor_gray));
    }
    //选中颜色
    private void setChoiceTextColor(TextView textView){
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.student_news_title1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.student_news_title2:
                viewPager.setCurrentItem(1);
                break;
        }
    }
}
