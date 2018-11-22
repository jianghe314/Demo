package com.kangzhan.student.Teacher;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kangzhan.student.BuildConfig;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.Student.Student_MainActivity;
import com.kangzhan.student.Teacher.Adapter.TeachermainFragment;
import com.kangzhan.student.Teacher.Fragment.BookingFragment;
import com.kangzhan.student.Teacher.Fragment.ClassFragment;
import com.kangzhan.student.Teacher.Fragment.MessageFragment;
import com.kangzhan.student.Teacher.Fragment.TrainFragment;
import com.kangzhan.student.Teacher.person_data.Mydata;
import com.kangzhan.student.Teacher.person_data.Mystudent;
import com.kangzhan.student.Teacher.person_data.TeacherPurseActivity;
import com.kangzhan.student.Teacher.person_data.Teacher_ChangePassword;
import com.kangzhan.student.Teacher.person_data.Teacher_Msg_bill;
import com.kangzhan.student.Teacher.person_data.Teacher_Reward;
import com.kangzhan.student.Teacher.person_data.Teacher_addAdvise;
import com.kangzhan.student.Teacher.person_data.Teacher_bill;
import com.kangzhan.student.Teacher_Login;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.kangzhan.student.utils.FileUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;


import java.io.File;
import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherMainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    //用户姓名，Id，和头像
    private TextView user_name,user_id;
    private CircleImageView user_header;
    private ArrayList<Fragment> fragmentList=new ArrayList<>();
    private DrawerLayout drawer;
    //
    private ViewPager mainViewPager;
    private TextView booking_tv,train_tv,exam_tv,news_tv,name;
    private ImageView booking_btn,train_btn,exam_btn,news_btn;
    private RelativeLayout r1,r2,r3,r4;
    //更换头像
    private File mTmpFile;
    private File mCropImageFile;
    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY  = 101;
    private static final int REQUEST_CROP  = 102;
    private BottomSheetDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.teacher_toolbar);
        setSupportActionBar(toolbar);
        initView();
        initViewPager();
        drawer = (DrawerLayout) findViewById(R.id.teacher_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.teacher_nav_view);
        View headerLayout=navigationView.getHeaderView(0);
        user_header= (CircleImageView) headerLayout.findViewById(R.id.teacher_header);
        Glide.with(TeacherMainActivity.this).load(teacher.teacherPhoto(TeacherMainActivity.this)).error(R.drawable.header).into(user_header);
        user_id= (TextView) headerLayout.findViewById(R.id.teacher_id);
        user_id.setText(teacher.teacherName(this)+"");
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initView() {
        booking_tv= (TextView) findViewById(R.id.teacher_booking_tv);
        booking_btn= (ImageView) findViewById(R.id.teacher_booking_btn);
        r1= (RelativeLayout) findViewById(R.id.teacher_r1);
        r1.setOnClickListener(this);
        train_tv= (TextView) findViewById(R.id.teacher_train_tv);
        train_btn= (ImageView) findViewById(R.id.teacher_train_btn);
        r2= (RelativeLayout) findViewById(R.id.teacher_r2);
        r2.setOnClickListener(this);
        exam_tv= (TextView) findViewById(R.id.teacher_exam_tv);
        exam_btn= (ImageView) findViewById(R.id.teacher_exam_btn);
        r3= (RelativeLayout) findViewById(R.id.teacher_r3);
        r3.setOnClickListener(this);
        news_tv= (TextView) findViewById(R.id.teacher_news_tv);
        news_btn= (ImageView) findViewById(R.id.teacher_news_btn);
        r4= (RelativeLayout) findViewById(R.id.teacher_r4);
        r4.setOnClickListener(this);
    }

    private void initViewPager() {
        fragmentList.add(new BookingFragment());
        fragmentList.add(new TrainFragment());
        fragmentList.add(new ClassFragment());
        fragmentList.add(new MessageFragment());
        mainViewPager= (ViewPager) findViewById(R.id.teacher_content_main_viewPager);
        //设置默认一次加载1个Fragment
        TeachermainFragment fragmentAdapter=new TeachermainFragment(getSupportFragmentManager(),fragmentList);
        mainViewPager.setAdapter(fragmentAdapter);
        mainViewPager.setOffscreenPageLimit(1);
        mainViewPager.setCurrentItem(0);
        clearImageAndTextColor();
        booking_btn.setBackgroundResource(R.mipmap.booking1);
        booking_tv.setTextColor(ContextCompat.getColor(this,R.color.textColor_evaluation));
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        clearImageAndTextColor();
                        booking_btn.setBackgroundResource(R.mipmap.booking1);
                        booking_tv.setTextColor(getResources().getColor(R.color.textColor_evaluation));
                        break;
                    case 1:
                        clearImageAndTextColor();
                        train_tv.setTextColor(getResources().getColor(R.color.textColor_evaluation));
                        train_btn.setBackgroundResource(R.mipmap.train1);
                        break;
                    case 2:
                        clearImageAndTextColor();
                        exam_btn.setBackgroundResource(R.mipmap.teacher_exam1);
                        exam_tv.setTextColor(getResources().getColor(R.color.textColor_evaluation));
                        break;
                    case 3:
                        clearImageAndTextColor();
                        news_tv.setTextColor(getResources().getColor(R.color.textColor_evaluation));
                        news_btn.setBackgroundResource(R.mipmap.info1);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void clearImageAndTextColor(){
        booking_btn.setBackgroundResource(R.mipmap.booking0);
        booking_tv.setTextColor(getResources().getColor(R.color.textColor_gray));
        //
        train_btn.setBackgroundResource(R.mipmap.train0);
        train_tv.setTextColor(getResources().getColor(R.color.textColor_gray));
        //
        exam_btn.setBackgroundResource(R.mipmap.teacher_exam0);
        exam_tv.setTextColor(getResources().getColor(R.color.textColor_gray));
        //
        news_btn.setBackgroundResource(R.mipmap.info0);
        news_tv.setTextColor(getResources().getColor(R.color.textColor_gray));
    }
    //侧拉菜单
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.teacher_mydata:
                Intent mydata=new Intent(this, Mydata.class);
                startActivity(mydata);
                break;
            case R.id.teacher_mystudent:
                Intent mystudent=new Intent(this, Mystudent.class);
                startActivity(mystudent);
                break;
            case R.id.teacher_chang_password:
                Intent password=new Intent(this, Teacher_ChangePassword.class);
                startActivity(password);
                break;
            case R.id.teacher_reward:
                Intent reward=new Intent(this, Teacher_Reward.class);
                startActivity(reward);
                break;
            case R.id.teacher_mypurse:
                if(teacher.teacherAttach(this).equals("0")){
                    Intent purse=new Intent(this, TeacherPurseActivity.class);
                    startActivity(purse);
                }else {
                    mToast.showText(getApplicationContext(),"只有独立教练才能用哦^_^");
                }

                break;
            case R.id.teacher_msg:
                Intent msg=new Intent(this, Teacher_Msg_bill.class);
                startActivity(msg);
                break;
            case R.id.teacher_addadvise:
                Intent advise=new Intent(this, Teacher_addAdvise.class);
                startActivity(advise);
                break;
            case R.id.teacher_bill:
                Intent bill=new Intent(this, Teacher_bill.class);
                startActivity(bill);
                break;
            case R.id.teacher_logout:
                JPushInterface.setAlias(this,"DEV_COACH_"+"",null);
                SharedPreferences sp=getSharedPreferences("teacherKey",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("key","false");
                editor.apply();
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_r1:
                mainViewPager.setCurrentItem(0,false);
                break;
            case R.id.teacher_r2:
                mainViewPager.setCurrentItem(1,false);
                break;
            case R.id.teacher_r3:
                mainViewPager.setCurrentItem(2,false);
                break;
            case R.id.teacher_r4:
                mainViewPager.setCurrentItem(3,false);
                break;
            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("您是要退出吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                //清除别名
                JPushInterface.setAlias(getApplicationContext(),"DEV_COACH_"+"",null);
                finish();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.create().show();
    }

}
