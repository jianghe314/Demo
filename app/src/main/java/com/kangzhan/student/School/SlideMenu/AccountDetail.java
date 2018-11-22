package com.kangzhan.student.School.SlideMenu;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.AccountManage;
import com.kangzhan.student.com.BaseActivity;

public class AccountDetail extends BaseActivity {
    private AccountManage item;
    private TextView name,account,ID,stastu,recordName,recordTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_account_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        item= (AccountManage) getData.getSerializableExtra("who");
        initView();
        initData();
    }



    private void initView() {
        name= (TextView) findViewById(R.id.school_account_detail_name);
        account= (TextView) findViewById(R.id.school_account_detail_account);
        ID= (TextView) findViewById(R.id.school_account_detail_ID);
        stastu= (TextView) findViewById(R.id.school_account_detail_stastus);
        recordName= (TextView) findViewById(R.id.school_account_detail_recordP);
        recordTime= (TextView) findViewById(R.id.school_account_detail_recordT);
    }

    private void initData() {
        name.setText(item.getReal_name());
        ID.setText(item.getIdcard());
        stastu.setText(item.getStatus());

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
