package com.kangzhan.student.HomeFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.HomeFragment.Adapter.FragmentAdapter;
import com.kangzhan.student.HomeFragment.Bean.EventMessage;
import com.kangzhan.student.HomeFragment.Bean.SchoolList;
import com.kangzhan.student.HomeFragment.HomeInterface.HomeInterface;
import com.kangzhan.student.HomeFragment.activities.SelectCityActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.com.NoScrollViewPager;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by kangzhan011 on 2017/11/16.
 */

public class FindSFragment extends HomeBaseFragment implements View.OnClickListener{
    private RelativeLayout byRemark,byDistence;
    private View tag;
    private TextView remark_tv,distance_tv,search,address;
    private EditText edit;
    private ViewPager viewPager;
    private SchoolFragment school1;
    private SchoolFragment2 school2;
    private int screenWidth;
    private int currentIndex;

    @Override
    protected int setContentView() {
        return R.layout.findsfragment_layout;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage msg){
        if(msg.getMsg().equals("1111")){

        }else if(msg.getMsg().equals("set_city")){
            address.setText(HomeInterface.getCurrentCity(getContext().getApplicationContext()));
            saveLocation(address.getText().toString().trim());

        }else if(msg.getMsg().equals("has_located")){
            //设置定位地址
            address.setText(HomeInterface.getHomeLocation(getContext().getApplicationContext())[0]);
            saveLocation(address.getText().toString().trim());
        }
    }

    @Override
    protected void initView(View v) {
        EventBus.getDefault().register(this);
        search= (TextView) v.findViewById(R.id.findsfrgment_by_search);
        search.setOnClickListener(this);
        edit= (EditText) v.findViewById(R.id.finds_search_et);
        address= (TextView) v.findViewById(R.id.finds_location_tv);
        address.setOnClickListener(this);
        byRemark= (RelativeLayout) v.findViewById(R.id.findsfrgment_by_remark);
        byDistence= (RelativeLayout) v.findViewById(R.id.findsfrgment_by_distence);
        tag=v.findViewById(R.id.findsfrgment_order_tag);
        remark_tv= (TextView) v.findViewById(R.id.findsfrgment_by_remark_tv);
        distance_tv= (TextView) v.findViewById(R.id.findsfrgment_by_distance_tv);
        viewPager= (ViewPager) v.findViewById(R.id.findsfrgment_viewPager);
        byRemark.setOnClickListener(this);
        byDistence.setOnClickListener(this);
        initWidth();

    }
    @Override
    protected void initData() {
        school1=new SchoolFragment();
        school2=new SchoolFragment2();
        ArrayList<Fragment> fragments=new ArrayList<>();
        fragments.add(school1);
        fragments.add(school2);
        FragmentAdapter adapter=new FragmentAdapter(getChildFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        remark_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tag.getLayoutParams();
                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2))+screenWidth/6;

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - positionOffset)
                            * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2))+screenWidth/6;

                }
                tag.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                currentIndex=position;
                switch (position){
                    case 0:
                        clearStyle();
                        remark_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        viewPager.setCurrentItem(0,false);
                        break;
                    case 1:
                        clearStyle();
                        distance_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        viewPager.setCurrentItem(1,false);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initWidth() {
        //这两个方法都能获取屏幕宽度
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        //获取控件的布局参数
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tag.getLayoutParams();
        lp.width=screenWidth/6;
        lp.leftMargin=screenWidth/6;
        tag.setLayoutParams(lp);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //搜索按钮
            case R.id.findsfrgment_by_search:
                //将事件传递到SchoolFragment
                saveSearch(edit.getText().toString().trim());
                EventBus.getDefault().post(new EventMessage("school_search"));
                break;
            case R.id.findsfrgment_by_remark:
                viewPager.setCurrentItem(0,true);
                break;
            case R.id.findsfrgment_by_distence:
                viewPager.setCurrentItem(1,true);
                break;
            case R.id.finds_location_tv:
                Intent loaction=new Intent(getContext(), SelectCityActivity.class);
                getActivity().startActivity(loaction);
                break;
        }

    }

    private void saveLocation(String city) {
        SharedPreferences sp=getContext().getSharedPreferences("Location",MODE_PRIVATE);
        SharedPreferences.Editor edtor=sp.edit();
        edtor.putString("city",city);
        edtor.apply();
    }

    private void saveSearch(String content) {
        SharedPreferences sp=getContext().getSharedPreferences("Home_Search",MODE_PRIVATE);
        SharedPreferences.Editor edtor=sp.edit();
        edtor.putString("content",content==""?"搜索内容为空":content);
        edtor.apply();
    }

    private void clearStyle(){
        remark_tv.setTextColor(getResources().getColor(R.color.textColor_black));
        distance_tv.setTextColor(getResources().getColor(R.color.textColor_black));
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
