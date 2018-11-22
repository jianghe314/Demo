package com.kangzhan.student.CompayManage.InfoManage;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kangzhan.student.R;

public class AllocateClerkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate_clerk);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_allocate_clerk_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        iniView();
    }

    private void iniView() {

    }
}
