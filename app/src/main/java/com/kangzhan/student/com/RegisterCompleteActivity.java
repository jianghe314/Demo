package com.kangzhan.student.com;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.MainLoginActivity;
import com.kangzhan.student.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterCompleteActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mtry,mtryN;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complete);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_studentRegist_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        mtry= (ImageView) findViewById(R.id.try_pay);
        mtry.setOnClickListener(this);
        mtryN= (ImageView) findViewById(R.id.try_no_pay);
        mtryN.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            ShowDialog(getRegistInfo()[0],getRegistInfo()[1]);
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.try_pay:
                Intent pay=new Intent(RegisterCompleteActivity.this,RegistExchangeActivity.class);
                startActivity(pay);
                finish();
                break;
            case R.id.try_no_pay:
                //弹一个注册成功的对话框
                //调到主页面，但不要新建一个主页面
                finish();
                break;
        }
    }

    private void ShowDialog(String name,String phone){
        //dialog有一个灰色底边
        popupWindow=new PopupWindow(this);
        View dialog=getLayoutInflater().inflate(R.layout.item_regist_dialog,null);
        popupWindow.setContentView(dialog);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView title= (TextView) dialog.findViewById(R.id.item_regist_dialog_title);
        ImageView back= (ImageView) dialog.findViewById(R.id.item_regist_dialog_back);
        TextView info= (TextView) dialog.findViewById(R.id.item_regist_dialog_info);
        title.setText("注册成功！");
        String Info="业务员<font color='#ff001e'>"+name+"</font>(电话<font color='#ff001e'>"+phone+"</font>)";
        info.setText(Html.fromHtml(Info));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对话框取消
               popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER,0,0);
    }


    private String[] getRegistInfo(){
        String[] mm = new String[2];
        SharedPreferences sp=getSharedPreferences("RegistInfo",MODE_PRIVATE);
        String data= sp.getString("Regist_Info","");
        if(!data.equals("")){
            try {
                JSONObject object=new JSONObject(data);
                mm[0]=object.getString("real_name");
                mm[1]=object.getString("phone");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mm;
    }
}
