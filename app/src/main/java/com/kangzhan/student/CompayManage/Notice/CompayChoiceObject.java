package com.kangzhan.student.CompayManage.Notice;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kangzhan.student.CompayManage.Adapter.mainFragment;
import com.kangzhan.student.CompayManage.Bean.CompayChoiceSchool;
import com.kangzhan.student.CompayManage.Bean.CompayChoiceStu;
import com.kangzhan.student.CompayManage.Bean.CompayChoiceTea;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.com.BaseActivity;

import java.util.ArrayList;

public class CompayChoiceObject extends BaseActivity implements View.OnClickListener,choiceStuFragment.iCompayChoiceStu,choiceTeaFragment.iCompayChoiceTea ,choiceSchoolFragment.iCompayChoiceSch {
    private TextView title1,title2,title3;
    private ViewPager viewPager;
    private View line;
    private mainFragment fragmentAdapter;
    private int currentIndex,screenWidth,mWidth;
    private ArrayList<CompayChoiceStu> data1=new ArrayList<>();
    private ArrayList<CompayChoiceTea> data2=new ArrayList<>();
    private ArrayList<CompayChoiceSchool> data3=new ArrayList<>();
    private MenuItem menuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compay_choice_object);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_choice_obj_toolbar);
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
        title1= (TextView) findViewById(R.id.compay_choice_student);
        title2= (TextView) findViewById(R.id.compay_choice_teacher);
        title3= (TextView) findViewById(R.id.compay_choice_school);
        title1.setOnClickListener(this);
        title2.setOnClickListener(this);
        title3.setOnClickListener(this);
        viewPager= (ViewPager) findViewById(R.id.compay_choice_viewPager);
        line=findViewById(R.id.compay_choice_line);
    }
    private void initWidth() {
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth=dm.widthPixels;
        LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) line.getLayoutParams();
        //设置横线的宽度
        //横线的初始边距
        mWidth=(screenWidth/3);
        lp.leftMargin=mWidth;
        lp.width=mWidth;
        line.setLayoutParams(lp);
    }

    private void initViewPager() {
        ArrayList<Fragment> fragmentList=new ArrayList<>();
        choiceStuFragment stufragment=new choiceStuFragment();
        choiceTeaFragment teafragment=new choiceTeaFragment();
        choiceSchoolFragment schoolfragmnet=new choiceSchoolFragment();
        fragmentList.add(stufragment);
        fragmentList.add(teafragment);
        fragmentList.add(schoolfragmnet);
        stufragment.choiceStufragment(this);
        teafragment.choiceTeafragment(this);
        schoolfragmnet.choiceSchoolfragment(this);
        fragmentAdapter=new mainFragment(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        setTextView(title1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) line.getLayoutParams();
                //Log.e("PageScroll","-->position："+position+"--->posOffset："+positionOffset);
                //0-->1
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
                else if(currentIndex==2&&position==2){         //2>3
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 3) + currentIndex
                            * (screenWidth / 3));
                }else if(currentIndex==3&&position==2){                      //3>2
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
                        clearTextView();
                        setTextView(title1);
                        break;
                    case 1:
                        clearTextView();
                        setTextView(title2);
                        break;
                    case 2:
                        clearTextView();
                        setTextView(title3);
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
            case R.id.compay_choice_student:
                viewPager.setCurrentItem(0);
                break;
            case R.id.compay_choice_teacher:
                viewPager.setCurrentItem(1);
                break;
            case R.id.compay_choice_school:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    private void clearTextView(){
        title1.setTextColor(ContextCompat.getColor(this,R.color.textColor_gray));
        title2.setTextColor(ContextCompat.getColor(this,R.color.textColor_gray));
        title3.setTextColor(ContextCompat.getColor(this,R.color.textColor_gray));
    }
    private void setTextView(TextView txt){
        txt.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sure,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_sure){
            if(data1.size()>0||data2.size()>0||data3.size()>0){
                String[] strName=new String[3];
                String[] strId=new String[3];
                String[] strPhone=new String[3];
                if(data1.size()>0){
                    StringBuilder name=new StringBuilder();
                    StringBuilder Id=new StringBuilder();
                    StringBuilder phone=new StringBuilder();
                    for (int i = 0; i <data1.size(); i++) {
                        name.append(data1.get(i).getName());
                        name.append(",");
                        Id.append(data1.get(i).getId());
                        Id.append(",");
                        phone.append(data1.get(i).getPhone());
                        phone.append(",");
                    }
                    strName[0]=name.toString();
                    strId[0]=Id.toString();
                    strPhone[0]=phone.toString();
                }else {
                    strName[0]="";
                    strId[0]="";
                    strPhone[0]="";
                }
                if(data2.size()>0){
                    StringBuilder name=new StringBuilder();
                    StringBuilder Id=new StringBuilder();
                    StringBuilder phone=new StringBuilder();
                    for (int i = 0; i <data2.size() ; i++) {
                        name.append(data2.get(i).getName());
                        name.append(",");
                        Id.append(data2.get(i).getId());
                        Id.append(",");
                        phone.append(data2.get(i).getMobile());
                        phone.append(",");
                    }
                    strName[1]=name.toString();
                    strId[1]=Id.toString();
                    strPhone[1]=phone.toString();
                }else {
                    strName[1]="";
                    strId[1]="";
                    strPhone[1]="";
                }
                if(data3.size()>0){
                    StringBuilder name=new StringBuilder();
                    StringBuilder Id=new StringBuilder();
                    StringBuilder phone=new StringBuilder();
                    for (int i = 0; i <data3.size() ; i++) {
                        name.append(data3.get(i).getContact());
                        name.append(",");
                        Id.append(data3.get(i).getId());
                        Id.append(",");
                        phone.append(data3.get(i).getPhone());
                        phone.append(",");
                    }
                    strName[2]=name.toString();
                    strId[2]=Id.toString();
                    strPhone[2]=phone.toString();
                }else {
                    strName[2]="";
                    strId[2]="";
                    strPhone[2]="";
                }
                Bundle bundle=new Bundle();
                bundle.putStringArray("Name",strName);
                bundle.putStringArray("Id",strId);
                bundle.putStringArray("Phone",strPhone);
                Intent add=new Intent();
                add.putExtra("data",bundle);
                setResult(1,add);
                finish();
            }else {
                mToast.showText(getApplicationContext(),"您还没有点击下面的添加！");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //接口回调
    @Override
    public void ChoiceStu(ArrayList<CompayChoiceStu> data) {
        data1.clear();
        if(data.size()>0){
            data1=data;
        }
        for (int i = 0; i < data.size(); i++) {
            mLog.e("学员回调结果","---->"+data.get(i).getName());
        }
    }

    @Override
    public void choiceTea(ArrayList<CompayChoiceTea> data) {
        data2.clear();
        if(data.size()>0){
            data2=data;
        }
        for (int i = 0; i < data.size(); i++) {
            mLog.e("教练回调结果","---->"+data.get(i).getName());
        }
    }

    @Override
    public void choiceSchool(ArrayList<CompayChoiceSchool> data) {
        data3.clear();
        if(data.size()>0){
            data3=data;
        }
        for (int i = 0; i < data.size(); i++) {
            mLog.e("驾校回调结果","---->"+data.get(i).getName());
        }
    }
}
