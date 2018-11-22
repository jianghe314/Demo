package com.kangzhan.student.CompayManage.InfoManage;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.kangzhan.student.R;
import com.kangzhan.student.com.BaseActivity;

public class CarInqueryActivity extends BaseActivity implements View.OnClickListener{
    //查看教练车详情是不是可以以对话框显示
    private EditText searchContent;
    private TextView doSearch;
    private PullRecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_inquery);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_CarInquery_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        searchContent= (EditText) findViewById(R.id.compay_CarInquery_searchContent);
        doSearch= (TextView) findViewById(R.id.compay_CarInquery_toSearch);
        recycler= (PullRecyclerView) findViewById(R.id.compay_CarInquery_recycler);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.compay_CarInquery_toSearch:
                //查询
                break;

        }
    }
}
