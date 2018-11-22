package com.kangzhan.student.School.Notice;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.noticeFragmentAdapter;
import com.kangzhan.student.School.Bean.NoticeChoiceStu;
import com.kangzhan.student.School.Bean.NoticeChoiceTea;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.com.BaseActivity;

import java.util.ArrayList;

public class School_Notice_Choice_Object extends BaseActivity implements View.OnClickListener,fragment_student_list.ChoiceStudent,fragment_teacher_list.ChoiceTeacher{
    private View line;
    private LinearLayout choiceBtn;
    private TextView title1,title2;
    private ViewPager viewPager;
    private noticeFragmentAdapter fragmentAdapter;
    private int currentIndex,ScreenWidth,mWidth;
    private ArrayList<NoticeChoiceStu> Studata=new ArrayList<>();
    private ArrayList<NoticeChoiceTea> Teadata=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school__notice__choice__object);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_notice_choice_object_toolbar);
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
        line=findViewById(R.id.school_notice_list_line);
        viewPager= (ViewPager) findViewById(R.id.school_notice_choice_object_viewPager);
        title1= (TextView) findViewById(R.id.school_notice_choice_object_title1);
        title2= (TextView) findViewById(R.id.school_notice_choice_object_title2);
        title1.setOnClickListener(this);
        title2.setOnClickListener(this);

    }

    private void initWidth() {
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ScreenWidth=dm.widthPixels;
        LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) line.getLayoutParams();
        //设置横线的宽度
        //横线的初始边距
        mWidth=(ScreenWidth/2);
        mWidth=mWidth/2;
        lp.leftMargin=mWidth;
        lp.width=mWidth;
        line.setLayoutParams(lp);
    }

    private void initViewPager() {
        ArrayList<Fragment> fragments=new ArrayList<>();
        fragment_teacher_list teacher_list=new fragment_teacher_list();
        fragment_student_list student_list=new fragment_student_list();
        teacher_list.FragmentChoiceTeahcer(this);
        student_list.FragmentChoiceStudent(this);
        fragments.add(student_list);
        fragments.add(teacher_list);
        fragmentAdapter=new noticeFragmentAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(fragmentAdapter);
        setClearColor();
        setTextColor(title1);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) line.getLayoutParams();
                //Log.e("postion-Current-Offset","->"+position+"-"+currentIndex+"-"+positionOffset);
                if(currentIndex==0&&position==0){      //0->1
                    lp.leftMargin=(int)((positionOffset*(ScreenWidth/2)+currentIndex*(ScreenWidth/2))+mWidth/2);
                    line.setLayoutParams(lp);
                }else if(currentIndex==1&&position==0){         //1->0
                    lp.leftMargin=(int)(-(1-positionOffset)*ScreenWidth/2+currentIndex*(ScreenWidth/2)+mWidth/2);
                    line.setLayoutParams(lp);
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentIndex=position;
                switch (position){
                    case 0:
                        setClearColor();
                        setTextColor(title1);
                        break;
                    case 1:
                        setClearColor();
                        setTextColor(title2);
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
            case R.id.school_notice_choice_object_title1:
                viewPager.setCurrentItem(0,true);
                break;
            case R.id.school_notice_choice_object_title2:
                viewPager.setCurrentItem(1,true);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sure,menu);
        //menuItem=menu.findItem(R.id.teacher_choice_student_menu);
        return true;
    }



    private void setTextColor(TextView txt){
        txt.setTextColor(ContextCompat.getColor(School_Notice_Choice_Object.this,R.color.colorPrimary));
    }
    private void setClearColor(){
        title1.setTextColor(ContextCompat.getColor(School_Notice_Choice_Object.this,R.color.textColor_gray));
        title2.setTextColor(ContextCompat.getColor(School_Notice_Choice_Object.this,R.color.textColor_gray));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_sure:
                if(Studata.size()>0||Teadata.size()>0){
                    StringBuilder builder1=new StringBuilder();
                    StringBuilder StuId=new StringBuilder();
                    StringBuilder TeaId=new StringBuilder();
                    String studentId="";
                    String teacherId="";
                    if(Studata.size()>0){
                        for (int i = 0; i < Studata.size(); i++) {
                            builder1.append(Studata.get(i).getPhone());
                            StuId.append(Studata.get(i).getId());
                            builder1.append(",");
                            StuId.append(",");
                        }
                        studentId=StuId.toString().substring(0,StuId.toString().length()-1);
                    }
                    if(Teadata.size()>0){
                        for (int i = 0; i < Teadata.size(); i++) {
                            builder1.append(Teadata.get(i).getMobile());
                            TeaId.append(Teadata.get(i).getId());
                            builder1.append(",");
                            TeaId.append(",");
                        }
                        teacherId=TeaId.toString().substring(0,TeaId.toString().length()-1);
                    }
                    String phones=builder1.toString().substring(0,builder1.toString().length()-1);
                    Intent add=new Intent();
                    Bundle data=new Bundle();
                    data.putString("phones",phones);
                    data.putString("studentId",studentId);
                    data.putString("teacherId",teacherId);
                    add.putExtra("data",data);
                    setResult(1,add);
                    finish();
                }else {
                    mToast.showText(getApplicationContext(),"您还没有点击下面的添加！");
                }

                break;
        }
        return true;
    }


    @Override
    public void choiceStudent(ArrayList<NoticeChoiceStu> data) {
        Studata.clear();
        for (int i = 0; i <data.size(); i++) {
            if(data.get(i).isClick()){
                Studata.add(data.get(i));
            }
        }
    }

    @Override
    public void choiceTeacher(ArrayList<NoticeChoiceTea> data) {
        Teadata.clear();
        for (int i = 0; i <data.size(); i++) {
            if(data.get(i).isClick()){
                Teadata.add(data.get(i));
            }
        }
    }
}
