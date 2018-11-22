package com.kangzhan.student.HomeFragment.mDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kangzhan.student.R;

/**
 * Created by kangzhan011 on 2017/11/21.
 */

public class IknowDialog extends Dialog {
    private Button btn;
    private TextView msg_tv;
    public IknowDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_dialog_question);
        setCanceledOnTouchOutside(true);
        initView();
    }
    public void setMsg(String msg){
        msg_tv.setText(msg);
    }

    private void initView() {
        msg_tv= (TextView) findViewById(R.id.home_dialog_question_content);
        btn= (Button) findViewById(R.id.home_dialog_question_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
