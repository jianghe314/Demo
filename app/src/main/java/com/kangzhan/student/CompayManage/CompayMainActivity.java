package com.kangzhan.student.CompayManage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kangzhan.student.CompayManage.Adapter.mainFragment;
import com.kangzhan.student.CompayManage.Fragment.AccountFragment;
import com.kangzhan.student.CompayManage.Fragment.InfoFragment;
import com.kangzhan.student.CompayManage.Fragment.SelfRegistFragment;
import com.kangzhan.student.CompayManage.Fragment.TeachingFragment;
import com.kangzhan.student.CompayManage.Notice.CompayMsgActivity;
import com.kangzhan.student.CompayManage.Notice.CompayNoticeActivity;
import com.kangzhan.student.CompayManage.SlideMenu.CompayChangePsdActivity;
import com.kangzhan.student.CompayManage.SlideMenu.CompayManageActivity;
import com.kangzhan.student.CompayManage.SlideMenu.Compay_AccountInfo;
import com.kangzhan.student.R;
import com.kangzhan.student.com.BaseActivity;

import java.util.ArrayList;

import static android.support.design.R.attr.actionBarSize;

public class CompayMainActivity extends BaseActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{
    private RelativeLayout content1,content2,content3,content4;
    private ImageView iv1,iv2,iv3,iv4;
    private TextView title1,title2,title3,title4;
    private ViewPager viewPager;
    private mainFragment fragmentAdapter;
    private MenuItem menuItem;
    private PopupWindow popupWindow;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compay_main);
        toolbar = (Toolbar) findViewById(R.id.compay_toolbar);
        setSupportActionBar(toolbar);
        initView();
        initViewPager();
        drawer = (DrawerLayout) findViewById(R.id.compay_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //
        NavigationView navigationView = (NavigationView) findViewById(R.id.compay_nav_view);
       navigationView.setNavigationItemSelectedListener(this);
    }

    private void initView() {
        viewPager= (ViewPager) findViewById(R.id.compay_content_main_viewPager);
        content1= (RelativeLayout) findViewById(R.id.compay_r1);
        content2= (RelativeLayout) findViewById(R.id.compay_r2);
        content3= (RelativeLayout) findViewById(R.id.compay_r3);
        content4= (RelativeLayout) findViewById(R.id.compay_r4);
        content1.setOnClickListener(this);
        content2.setOnClickListener(this);
        content3.setOnClickListener(this);
        content4.setOnClickListener(this);
        iv1= (ImageView) findViewById(R.id.compay_title_iv1);
        iv2= (ImageView) findViewById(R.id.compay_title_iv2);
        iv3= (ImageView) findViewById(R.id.compay_title_iv3);
        iv4= (ImageView) findViewById(R.id.compay_title_iv4);
        title1= (TextView) findViewById(R.id.compay_title_tv1);
        title2= (TextView) findViewById(R.id.compay_title_tv2);
        title3= (TextView) findViewById(R.id.compay_title_tv3);
        title4= (TextView) findViewById(R.id.compay_title_tv4);
    }
    private void initViewPager() {
        ArrayList<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(new SelfRegistFragment());
        fragmentList.add(new TeachingFragment());
        fragmentList.add(new AccountFragment());
        fragmentList.add(new InfoFragment());
        fragmentAdapter=new mainFragment(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0,false);
        iv1.setImageResource(R.mipmap.compay_mine1);
        title1.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        clearColor();
                        iv1.setImageResource(R.mipmap.compay_mine1);
                        title1.setTextColor(ContextCompat.getColor(CompayMainActivity.this,R.color.colorPrimary));
                        break;
                    case 1:
                        clearColor();
                        iv2.setImageResource(R.mipmap.teacher_exam1);
                        title2.setTextColor(ContextCompat.getColor(CompayMainActivity.this,R.color.colorPrimary));
                        break;
                    case 2:
                        clearColor();
                        iv3.setImageResource(R.mipmap.compay_money1);
                        title3.setTextColor(ContextCompat.getColor(CompayMainActivity.this,R.color.colorPrimary));
                        break;
                    case 3:
                        clearColor();
                        iv4.setImageResource(R.mipmap.compay_info1);
                        title4.setTextColor(ContextCompat.getColor(CompayMainActivity.this,R.color.colorPrimary));
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
            case R.id.compay_r1:
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.compay_r2:
                viewPager.setCurrentItem(1,false);
                break;
            case R.id.compay_r3:
                viewPager.setCurrentItem(2,false);
                break;
            case R.id.compay_r4:
                viewPager.setCurrentItem(3,false);
                break;
            case R.id.compay_notice_menu_notice:
                popupWindow.dismiss();
                Intent notice=new Intent(this, CompayNoticeActivity.class);
                startActivity(notice);
                break;
            case R.id.compay_notice_menu_msg:
                popupWindow.dismiss();
                Intent msg=new Intent(this, CompayMsgActivity.class);
                startActivity(msg);
                break;

        }
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
            View menu= LayoutInflater.from(this).inflate(R.layout.item_compay_notice_menu,null);
            popupWindow=new PopupWindow(CompayMainActivity.this);
            popupWindow.setContentView(menu);
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout menu1= (RelativeLayout) menu.findViewById(R.id.compay_notice_menu_notice);
            RelativeLayout menu2= (RelativeLayout) menu.findViewById(R.id.compay_notice_menu_msg);
            menu1.setOnClickListener(this);
            menu2.setOnClickListener(this);
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAtLocation(toolbar, Gravity.RIGHT|Gravity.TOP,0,100);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.compay_draw_school:
                Intent info=new Intent(this, Compay_AccountInfo.class);
                startActivity(info);
                break;
            case R.id.compay_draw_manage:
                Intent manage=new Intent(this, CompayManageActivity.class);
                startActivity(manage);
                break;
            case R.id.compay_draw_changPsd:
                Intent psd=new Intent(this, CompayChangePsdActivity.class);
                startActivity(psd);
                break;
            case R.id.compay_draw_logout:
                //清除登录key
                SharedPreferences sp=getSharedPreferences("compayKey",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("key","false");
                editor.apply();
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearColor() {
        iv1.setImageResource(R.mipmap.compay_mine);
        iv2.setImageResource(R.mipmap.teacher_exam0);
        iv3.setImageResource(R.mipmap.compay_money);
        iv4.setImageResource(R.mipmap.compay_info);
        title1.setTextColor(ContextCompat.getColor(this,R.color.texColor_Text));
        title2.setTextColor(ContextCompat.getColor(this,R.color.texColor_Text));
        title3.setTextColor(ContextCompat.getColor(this,R.color.texColor_Text));
        title4.setTextColor(ContextCompat.getColor(this,R.color.texColor_Text));
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

}
