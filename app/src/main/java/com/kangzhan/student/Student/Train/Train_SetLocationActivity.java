package com.kangzhan.student.Student.Train;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kangzhan.student.Student.Booking.TeacherListActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.entity.City;
import com.kangzhan.student.entity.County;
import com.kangzhan.student.entity.Province;
import com.kangzhan.student.utils.AddressPickTask;

public class Train_SetLocationActivity extends BaseActivity implements View.OnClickListener{
    private TextView setlocation,setCurrentLocation;
    private Button sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train__set_location);
        Toolbar toolbar= (Toolbar) findViewById(R.id.setLocation_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void initView() {
        setlocation= (TextView) findViewById(R.id.set_location);
        setlocation.setOnClickListener(this);
        sure= (Button) findViewById(R.id.set_location_sure);
        sure.setOnClickListener(this);
        setCurrentLocation= (TextView) findViewById(R.id.getCurrent_location);
        setCurrentLocation.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_location:
                //选择地区
                AddressPickTask task=new AddressPickTask(this);
                task.setHideCounty(false);
                task.setHideProvince(false);
                task.setCallback(new AddressPickTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
                        showToast("数据初始化失败");
                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        if (county == null) {
                            // showToast(province.getAreaName() + city.getAreaName());
                            setlocation.setText(province.getAreaName()+city.getAreaName());
                        } else {
                            //showToast(province.getAreaName() + city.getAreaName() + county.getAreaName());
                            setlocation.setText(province.getAreaName()+city.getAreaName()+county.getAreaName());
                        }
                    }
                });
                task.execute("北京市", "北京市", "东城区");
                break;
            //使用当前位置
            case R.id.getCurrent_location:
                //掉GPS定位当前位置
                break;
            //确定
            case R.id.set_location_sure:
                //将位置传递给教练列表
                String location=setlocation.getText().toString().trim();
                Intent getMore=new Intent(this,Train_applyActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("location",location);
                getMore.putExtra("location",bundle);
                startActivity(getMore);
                break;
            default:
                break;
        }
    }


}
