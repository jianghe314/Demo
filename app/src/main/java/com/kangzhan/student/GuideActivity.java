package com.kangzhan.student;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.kangzhan.student.Adapter.GuideAdapter;
import com.kangzhan.student.HeadView.HeadViewActivity;
import com.kangzhan.student.com.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity implements View.OnClickListener{
    private ViewPager viewPager;
    private ImageView iv1,iv2,iv3;
    private Button startExp;
    private int[] background={R.drawable.guide_iv1,R.drawable.guide_iv2,R.drawable.guide_iv3};
    private GuideAdapter adapter;
    private ArrayList<View> itemView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initViewPager();
    }

    private void initView() {
        viewPager= (ViewPager) findViewById(R.id.guide_viewPager);
        iv1= (ImageView) findViewById(R.id.indicator1);
        iv2= (ImageView) findViewById(R.id.indicator2);
        iv3= (ImageView) findViewById(R.id.indicator3);
        startExp= (Button) findViewById(R.id.guide_btn);
        startExp.setOnClickListener(this);
        itemView=new ArrayList<>();
        for (int i = 0; i <background.length; i++) {
            View view= LayoutInflater.from(this).inflate(R.layout.item_guide_image,null);
            ImageView iv= (ImageView) view.findViewById(R.id.item_guide_iv);
            Glide.with(this).load(background[i]).into(iv);
            itemView.add(view);
        }
        adapter=new GuideAdapter(GuideActivity.this,background,itemView);
        viewPager.setAdapter(adapter);
    }
    private void initViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        startExp.setVisibility(View.GONE);
                        setPoint();
                        clearPoint(iv1);
                        break;
                    case 1:
                        startExp.setVisibility(View.GONE);
                        setPoint();
                        clearPoint(iv2);
                        break;
                    case 2:
                        startExp.setVisibility(View.VISIBLE);
                        setPoint();
                        clearPoint(iv3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setPoint(){
        iv1.setImageResource(R.mipmap.point1);
        iv2.setImageResource(R.mipmap.point1);
        iv3.setImageResource(R.mipmap.point1);
    }
    private void clearPoint(ImageView iv){
        iv.setImageResource(R.mipmap.point0);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.guide_btn){
            Intent inte=new Intent(GuideActivity.this,HeadViewActivity.class);
            startActivity(inte);
            finish();
        }
    }
}
