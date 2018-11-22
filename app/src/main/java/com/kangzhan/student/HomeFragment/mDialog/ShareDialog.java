package com.kangzhan.student.HomeFragment.mDialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Button;

import com.kangzhan.student.R;

/**
 * Created by kangzhan011 on 2017/11/27.
 */

public class ShareDialog extends BottomSheetDialog {

    private Button share_person,share_session,share_cancel;
    private boolean  share_way;      //分享方式，true为分享朋友，false分享朋友圈
    private ShareWay shareWay;

    public ShareDialog(@NonNull Context context) {
        super(context, R.style.ChoiceHeadTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_layout);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        initView();
    }

    private void initView() {
        share_person= (Button) findViewById(R.id.share_person);
        share_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_way=true;
                shareWay.shareWay(share_way);
                dismiss();

            }
        });
        share_session= (Button) findViewById(R.id.share_session);
        share_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_way=false;
                shareWay.shareWay(share_way);
                dismiss();
            }
        });
        share_cancel= (Button) findViewById(R.id.share_cancel);
        share_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void getShareWay(ShareWay shareWay){
        if(shareWay!=null){
            this.shareWay=shareWay;
        }
    }


    public interface ShareWay{
        void shareWay(boolean way);
    }



}
