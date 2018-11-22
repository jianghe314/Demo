package com.kangzhan.student.Advisetment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.kangzhan.student.Advisetment.newsFragment.NewsAll;
import com.kangzhan.student.Advisetment.newsFragment.NewsBusiness;
import com.kangzhan.student.Advisetment.newsFragment.NewsOurs;
import com.kangzhan.student.Advisetment.newsFragment.NewsView;
import com.kangzhan.student.CompayManage.Adapter.mainFragment;
import com.kangzhan.student.HomeFragment.Bean.EventMessage;
import com.kangzhan.student.R;
import com.kangzhan.student.com.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class RecommendNewsActivity extends BaseActivity {
    private TextView all,ours,bus,mview;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_news);
        Toolbar toolbar= (Toolbar) findViewById(R.id.recommend_news_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        initData();
        String item=getIntent().getStringExtra("item");
        if(item.equals("2")){
            viewPager.setCurrentItem(2,false);
        }
    }

    private void initView() {
        all= (TextView) findViewById(R.id.recommend_news_all);
        ours= (TextView) findViewById(R.id.recommend_news_ours);
        bus= (TextView) findViewById(R.id.recommend_news_bus);
        mview= (TextView) findViewById(R.id.recommend_news_view);
        viewPager= (ViewPager) findViewById(R.id.recommend_news_viewPager);
    }

    private void initData() {
        ArrayList<Fragment> fragments=new ArrayList<>();
        fragments.add(new NewsAll());
        fragments.add(new NewsOurs());
        fragments.add(new NewsBusiness());
        fragments.add(new NewsView());
        mainFragment fragmentAdapter=new mainFragment(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        clearColor();
                        setTextColor(all);
                        break;
                    case 1:
                        clearColor();
                        setTextColor(ours);
                        break;
                    case 2:
                        clearColor();
                        setTextColor(bus);
                        break;
                    case 3:
                        clearColor();
                        setTextColor(mview);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setTextColor(TextView text){
        text.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
    }
    private void clearColor(){
        all.setTextColor(ContextCompat.getColor(this,R.color.texColor_Text));
        ours.setTextColor(ContextCompat.getColor(this,R.color.texColor_Text));
        bus.setTextColor(ContextCompat.getColor(this,R.color.texColor_Text));
        mview.setTextColor(ContextCompat.getColor(this,R.color.texColor_Text));
    }


}
