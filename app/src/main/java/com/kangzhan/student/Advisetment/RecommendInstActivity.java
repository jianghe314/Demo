package com.kangzhan.student.Advisetment;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.kangzhan.student.R;
import com.kangzhan.student.com.BaseActivity;

public class RecommendInstActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_inst);
        Toolbar toolbar= (Toolbar) findViewById(R.id.recommend_inst_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
