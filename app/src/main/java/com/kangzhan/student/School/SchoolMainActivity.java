package com.kangzhan.student.School;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.mainFragment;
import com.kangzhan.student.School.Fragment.Accountfragment;
import com.kangzhan.student.School.Fragment.Bookingfragment;
import com.kangzhan.student.School.Fragment.EduManagefragment;
import com.kangzhan.student.School.Fragment.Lessonfragment;
import com.kangzhan.student.School.Fragment.Trainingfragment;
import com.kangzhan.student.School.Notice.School_Notice_AdviseActivity;
import com.kangzhan.student.School.Notice.School_Notice_MsgActivity;
import com.kangzhan.student.School.Notice.School_Notice_NoticeActivity;
import com.kangzhan.student.School.SlideMenu.Add_Account;
import com.kangzhan.student.School.SlideMenu.School_Change_Psd;
import com.kangzhan.student.School.SlideMenu.School_Info;
import com.kangzhan.student.School.SlideMenu.School_Menu_Account;
import com.kangzhan.student.Student.Student_MainActivity;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.SchoolInterface.school;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class SchoolMainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{
    private RelativeLayout r1,r2,r3,r4,r5;
    private ImageView iv1,iv2,iv3,iv4,iv5;
    private TextView tv1,tv2,tv3,tv4,tv5,schoolName;
    private ViewPager viewPager;
    private PopupWindow popupWindow;
    private MenuItem menuItem;
    private DrawerLayout drawer;
    private ArrayList<Fragment> fragmentList=new ArrayList<>();
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        initMenu();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.school_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout=navigationView.getHeaderView(0);
        schoolName= (TextView)headerLayout.findViewById(R.id.school_slide_menu_schoolName);
        schoolName.setText(school.schoolName(this));
    }



    private void initView() {
        r1= (RelativeLayout) findViewById(R.id.school_r1);
        r1.setOnClickListener(this);
        r2= (RelativeLayout) findViewById(R.id.school_r2);
        r2.setOnClickListener(this);
        r3= (RelativeLayout) findViewById(R.id.school_r3);
        r3.setOnClickListener(this);
        r4= (RelativeLayout) findViewById(R.id.school_r4);
        r4.setOnClickListener(this);
        r5= (RelativeLayout) findViewById(R.id.school_r5);
        r5.setOnClickListener(this);
        iv1= (ImageView) findViewById(R.id.school_eduManage_btn);
        iv2= (ImageView) findViewById(R.id.school_lesson_btn);
        iv3= (ImageView) findViewById(R.id.school_booking_btn);
        iv4= (ImageView) findViewById(R.id.school_training_btn);
        iv5= (ImageView) findViewById(R.id.schhol_acount_btn);
        tv1= (TextView) findViewById(R.id.school_eduManage_tv);
        tv2= (TextView) findViewById(R.id.school_lesson_tv);
        tv3= (TextView) findViewById(R.id.school_booking_tv);
        tv4= (TextView) findViewById(R.id.school_training_tv);
        tv5= (TextView) findViewById(R.id.schhol_acount_tv);
        viewPager= (ViewPager) findViewById(R.id.school_main_viewPager);
    }

    private void initMenu() {
        fragmentList.add(new EduManagefragment());
        fragmentList.add(new Lessonfragment());
        fragmentList.add(new Bookingfragment());
        fragmentList.add(new Trainingfragment());
        fragmentList.add(new Accountfragment());
        mainFragment adapter=new mainFragment(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0,false);
        tv1.setTextColor(ContextCompat.getColor(SchoolMainActivity.this,R.color.colorPrimary));
        iv1.setBackgroundResource(R.mipmap.school_edu_manage1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        clearColor();
                        tv1.setTextColor(ContextCompat.getColor(SchoolMainActivity.this,R.color.colorPrimary));
                        iv1.setBackgroundResource(R.mipmap.school_edu_manage1);
                        break;
                    case 1:
                        clearColor();
                        tv2.setTextColor(ContextCompat.getColor(SchoolMainActivity.this,R.color.colorPrimary));
                        iv2.setBackgroundResource(R.mipmap.teacher_exam1);
                        break;
                    case 2:
                        clearColor();
                        tv3.setTextColor(ContextCompat.getColor(SchoolMainActivity.this,R.color.colorPrimary));
                        iv3.setBackgroundResource(R.mipmap.booking1);
                        break;
                    case 3:
                        clearColor();
                        tv4.setTextColor(ContextCompat.getColor(SchoolMainActivity.this,R.color.colorPrimary));
                        iv4.setBackgroundResource(R.mipmap.train1);
                        break;
                    case 4:
                        clearColor();
                        tv5.setTextColor(ContextCompat.getColor(SchoolMainActivity.this,R.color.colorPrimary));
                        iv5.setBackgroundResource(R.mipmap.acount1);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.school_main, menu);
        menuItem=menu.findItem(R.id.action_broadcast);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_broadcast){
            View menu= LayoutInflater.from(this).inflate(R.layout.item_school_notice_menu,null);
            popupWindow=new PopupWindow(SchoolMainActivity.this);
            popupWindow.setContentView(menu);
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout menu1= (RelativeLayout) menu.findViewById(R.id.school_notice_menu_notice);
            RelativeLayout menu2= (RelativeLayout) menu.findViewById(R.id.school_notice_menu_advise);
            RelativeLayout menu3= (RelativeLayout) menu.findViewById(R.id.school_notice_menu_msg);
            menu1.setOnClickListener(this);
            menu2.setOnClickListener(this);
            menu3.setOnClickListener(this);
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAtLocation(toolbar, Gravity.RIGHT|Gravity.TOP,0,100);
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // 侧拉菜单点击事件
        int id = item.getItemId();
        switch (id){
            case R.id.school_acount_manage:
                Intent manage=new Intent(this, School_Menu_Account.class);
                startActivity(manage);
                break;
            case R.id.school_info:
                Intent info=new Intent(this, School_Info.class);
                startActivity(info);
                break;
            case R.id.school_add_acount:
                Intent add=new Intent(this, Add_Account.class);
                startActivity(add);
                break;
            case R.id.school_change_psd:
                Intent psd=new Intent(this, School_Change_Psd.class);
                startActivity(psd);
                break;
            case R.id.school_logout:
                SharedPreferences sp=getSharedPreferences("schoolKey",MODE_PRIVATE);
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
        switch (v.getId()) {
            case R.id.school_r1:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.school_r2:
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.school_r3:
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.school_r4:
                viewPager.setCurrentItem(3, false);
                break;
            case R.id.school_r5:
                viewPager.setCurrentItem(4, false);
                break;
            case R.id.school_notice_menu_notice:
                popupWindow.dismiss();
                Intent notice=new Intent(this, School_Notice_NoticeActivity.class);
                startActivity(notice);
                break;
            case R.id.school_notice_menu_advise:
                popupWindow.dismiss();
                Intent advise=new Intent(this, School_Notice_AdviseActivity.class);
                startActivity(advise);
                break;
            case R.id.school_notice_menu_msg:
                popupWindow.dismiss();
                Intent msg=new Intent(this, School_Notice_MsgActivity.class);
                startActivity(msg);
                break;
            default:
                break;
        }
    }


    private void clearColor(){
        tv1.setTextColor(ContextCompat.getColor(SchoolMainActivity.this,R.color.texColor_Text));
        tv2.setTextColor(ContextCompat.getColor(SchoolMainActivity.this,R.color.texColor_Text));
        tv3.setTextColor(ContextCompat.getColor(SchoolMainActivity.this,R.color.texColor_Text));
        tv4.setTextColor(ContextCompat.getColor(SchoolMainActivity.this,R.color.texColor_Text));
        tv5.setTextColor(ContextCompat.getColor(SchoolMainActivity.this,R.color.texColor_Text));
        iv1.setBackgroundResource(R.mipmap.school_edu_manage0);
        iv2.setBackgroundResource(R.mipmap.teacher_exam0);
        iv3.setBackgroundResource(R.mipmap.booking0);
        iv4.setBackgroundResource(R.mipmap.train0);
        iv5.setBackgroundResource(R.mipmap.acount0);
    }

}
