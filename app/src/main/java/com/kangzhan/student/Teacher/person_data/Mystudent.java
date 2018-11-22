package com.kangzhan.student.Teacher.person_data;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;;
import android.widget.TextView;

import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.Adapter.TeachermainFragment;
import com.kangzhan.student.com.BaseActivity;

import java.util.ArrayList;


public class Mystudent extends BaseActivity implements View.OnClickListener{
    private TextView student_Num,title1,title2,title3;
    private View linespace;
    private ViewPager viewPager;
    private int screenWidth;
    private int currentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystudent);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_mystudent_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        initWidth();
        initViewPager();

    }

    private void initView() {
        title1= (TextView) findViewById(R.id.techer_mySudent_title1);
        title1.setOnClickListener(this);
        title2= (TextView) findViewById(R.id.techer_mySudent_title2);
        title2.setOnClickListener(this);
        title3= (TextView) findViewById(R.id.techer_mySudent_title3);
        title3.setOnClickListener(this);
        student_Num= (TextView) findViewById(R.id.teacher_myStudent_sum);
        linespace=findViewById(R.id.teacher_myStudent_linespace);
        viewPager= (ViewPager) findViewById(R.id.teacher_myStudent_viewpager);
    }

    private void initWidth() {
        //这两个方法都能获取屏幕宽度
        DisplayMetrics dpMetrics = new DisplayMetrics();
        Mystudent.this.getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        mLog.i("widths:-->",dpMetrics.widthPixels+"");
        screenWidth = dpMetrics.widthPixels;
        //获取控件的布局参数
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) linespace.getLayoutParams();
        lp.width=screenWidth/3;
        linespace.setLayoutParams(lp);
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(2);
        ArrayList<Fragment> fragments=new ArrayList<>();
        fragments.add(new MyStudentFragment1());
        fragments.add(new MyStudentFragment2());
        fragments.add(new MyStudentFragment3());
        TeachermainFragment pagerAdapter=new TeachermainFragment(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0,false);
        setTextColor();
        setChoiceTextColor(title1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //postion:位置  positionOffset:位置偏移  positionOffsetPixels：位置偏移像素
                mLog.i("xxxx","postion->"+position+"=positionOffset->"+positionOffset+"=positionOffsetPixels->"+positionOffsetPixels);
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
                linespace.setLayoutParams(lp);
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
            case R.id.techer_mySudent_title1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.techer_mySudent_title2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.techer_mySudent_title3:
                viewPager.setCurrentItem(2);
                break;
        }
    }

}
