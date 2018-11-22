package com.kangzhan.student.CompayManage.InfoManage;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.utils.picker.DateTimePicker;

public class CompaySendNotice extends BaseActivity implements View.OnClickListener {
    private EditText msgContent;
    private ImageView isNow,choiceTime;
    private TextView time;
    private boolean right=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compay_send_notice);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_send_notice_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        msgContent= (EditText) findViewById(R.id.compay_send_notice_content);
        isNow= (ImageView) findViewById(R.id.compay_send_notice_choice);
        choiceTime= (ImageView) findViewById(R.id.compay_send_notice_choiceTime);
        time= (TextView) findViewById(R.id.compay_send_notice_time);
        isNow.setOnClickListener(this);
        choiceTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.compay_send_notice_choice:
                if(right){
                    isNow.setImageResource(R.mipmap.teacher_news_sendnotice);
                    right=false;
                }else {
                    isNow.setImageResource(R.mipmap.teacher_news_choice);
                    right=true;
                }
                break;
            case R.id.compay_send_notice_choiceTime:
                DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
                picker.setDateRangeStart(2017, 1, 1);
                picker.setDateRangeEnd(2025, 11, 11);
                picker.setTimeRangeStart(00,00);
                picker.setTimeRangeEnd(23,59);
                picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                    @Override
                    public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                        time.setText(year+"-"+month+"-"+day+" "+hour+":"+minute);
                    }
                });
                picker.show();
                break;
            default:
                break;
        }
    }
}
