package com.kangzhan.student.HomeFragment.mDialog;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kangzhan.student.R;

/**
 * Created by kangzhan011 on 2017/11/21.
 */

public class JoinDialog extends Dialog {
    private TextView name,phone,classes;
    private Button send_on;
    private ImageView close;
    private String[] strings=new String[3];
    private SendOnClick sendOnClick;
    public JoinDialog(@NonNull Context context) {
        super(context,R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_dialog_join);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        name= (TextView) findViewById(R.id.home_dialog_join_name);
        phone= (TextView) findViewById(R.id.home_dialog_join_phone);
        classes= (TextView) findViewById(R.id.home_dialog_join_class);
        send_on= (Button) findViewById(R.id.home_dialog_join_btn);
        close= (ImageView) findViewById(R.id.home_dialog_join_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
       send_on.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sendOnClick.sendOnClick(getInfo());
               if(!getInfo()[0].equals("")&&!getInfo()[1].equals("")&&!getInfo()[2].equals("")){
                   dismiss();
               }
           }
       });
    }

    public void sendOnClick(SendOnClick sendOnClick){
        this.sendOnClick=sendOnClick;
    }

    private String[] getInfo(){
       strings[0]=name.getText().toString();
       strings[1]=phone.getText().toString();
       strings[2]=classes.getText().toString();
       return strings;
    }



    public interface SendOnClick{
        void sendOnClick(String[] info);
    }
}
