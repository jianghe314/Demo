package com.kangzhan.student.CompayManage.SelfRegistManage;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.kangzhan.student.R;
import com.kangzhan.student.com.BaseActivity;

public class ClerkListActivity extends BaseActivity {
    private PullRecyclerView recycler;
    private Button sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_list);
        Toolbar toolbar= (Toolbar) findViewById(R.id.self_regist_clerk_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        recycler= (PullRecyclerView) findViewById(R.id.self_regist_clerk_list_recycler);
        sure= (Button) findViewById(R.id.self_regist_clerk_list_sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}
