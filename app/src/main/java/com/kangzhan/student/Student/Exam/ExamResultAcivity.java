package com.kangzhan.student.Student.Exam;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.com.BaseActivity;

public class ExamResultAcivity extends BaseActivity {
    private TextView exam_result,exam_time;
    private Button exam_again;
    private int subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_result_acivity);
        Intent intent=getIntent();
        String time=intent.getStringExtra("time");
        String[] t=time.split(":");
        subject=intent.getIntExtra("subject",0);
        int h=Integer.valueOf(t[0]);
        int m=Integer.valueOf(t[1]);
        int grade=intent.getIntExtra("grade",0);
        Toolbar toolbar= (Toolbar) findViewById(R.id.exam_result_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        exam_result.setText(grade+"分");
        exam_time.setText("用时："+(45-h-1)+":"+(60-m));
    }


    private void initView() {
        exam_result= (TextView) findViewById(R.id.exam_result_tv);
        exam_time= (TextView) findViewById(R.id.exam_time_tv);
        exam_again= (Button) findViewById(R.id.exam_again);
    }
    public void goBack(View v){
        if(subject==1){
            startActivity(new Intent(this,Subject1Activity.class));
            finish();
        }else if(subject==4){
            startActivity(new Intent(this,Subject4Activity.class));
            finish();
        }

    }

}
