package com.kangzhan.student.HomeFragment.mDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kangzhan.student.R;

/**
 * Created by kangzhan011 on 2017/11/28.
 */

public class ErrorDialog extends Dialog {

    private TextView msg;
    private RelativeLayout btn;
    public ErrorDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_layout);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        msg= (TextView) findViewById(R.id.error_txt);
        btn= (RelativeLayout) findViewById(R.id.error_sure);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public void setTextMsg(String txt){
        msg.setText(txt);
    }

}
