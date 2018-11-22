package com.kangzhan.student.Student.News;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.item_simple_listAdapter;
import com.kangzhan.student.Student.bean.checkObject;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddAdviseActivity extends BaseActivity implements View.OnClickListener{
    private EditText title,content;
    private TextView choice;
    private Button holdOn;
    private int which;
    private item_simple_listAdapter adapter;
    private int adviseWho;
    private String WhoId,mmsg;
    private ArrayList<checkObject> mdata=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(getApplicationContext(),"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            final Dialog dialog=new Dialog(AddAdviseActivity.this);
                            dialog.setTitle("选择投诉对象");
                            View view= LayoutInflater.from(AddAdviseActivity.this).inflate(R.layout.item_simple_listview,null);
                            ListView listView= (ListView) view.findViewById(R.id.item_simple_list);
                            if(adapter==null){
                                adapter=new item_simple_listAdapter(AddAdviseActivity.this,R.layout.item_simple_list,mdata);
                            }
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    choice.setText(adapter.getItem(position).getName());
                                    WhoId=adapter.getItem(position).getId();
                                    dialog.dismiss();
                                }
                            });
                            dialog.setContentView(view);
                            dialog.show();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(getApplicationContext(),"上传中...");
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 8888:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(getApplicationContext(),"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advise);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_addadvise_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        Intent who=getIntent();
        which=who.getIntExtra("who",2);
        initView();
    }

    private void initView() {
        title= (EditText) findViewById(R.id.student_addadvise_title);
        content= (EditText) findViewById(R.id.student_addadvise_content);
        choice= (TextView) findViewById(R.id.student_addadvise_choice);
        choice.setOnClickListener(this);
        holdOn= (Button) findViewById(R.id.student_addadvise_holdOn);
        holdOn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.student_addadvise_choice:
                if(which==2){
                    AlertDialog.Builder builder=new AlertDialog.Builder(AddAdviseActivity.this);
                    builder.setItems(new String[]{"教练", "驾校"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int mwhich) {
                            which=mwhich;
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                } else if(which==0){           //which 0:教练  1：驾校
                    //Log.e("teacher","->走教练");
                    adviseWho=20;
                    handler.sendEmptyMessage(0000);
                    sendRequest(student.studentChoiceTeacher());
                }else if(which==1) {
                    //Log.e("teacher","->走驾校");
                    adviseWho=30;
                    handler.sendEmptyMessage(0000);
                    sendRequest(student.studentChoiceSchool());
                }
                break;
            case R.id.student_addadvise_holdOn:
                //上传
                if(isRight()){
                    sendInfo(student.studentSendAdvise(),"1",adviseWho,WhoId,title.getText().toString().trim(),content.getText().toString().trim());
                    handler.sendEmptyMessage(2222);
                }
                break;
            default:
                break;
        }
    }

    private boolean isRight() {
        boolean tit=false,con=false;
        if(title.getText().toString().trim().equals("")){
            Toast.makeText(AddAdviseActivity.this,"问题摘要不能为空",Toast.LENGTH_SHORT).show();
        }else {
            tit=true;
        }
        if(content.getText().toString().trim().equals("")){
            Toast.makeText(AddAdviseActivity.this,"问题描述不能为空",Toast.LENGTH_SHORT).show();
        }else {
            con=true;
        }
        if(tit&&con){
            return true;
        }else {
            return false;
        }
    }

    private void sendInfo(String url, String id, int type, final String objectId, String summary, String content) {
        final Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,RequestMethod.POST);
        request.add("key",student.studentKey(getApplicationContext()));
        request.add("stu_id",id);
        request.add("type",type);
        request.add("objeid",objectId);
        request.add("summary",summary);
        request.add("content",content);
        getRequestQueue().add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("advise","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    handler.sendEmptyMessage(3333);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                handler.sendEmptyMessage(9999);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void sendRequest(String url) {
        Request<String> request=NoHttp.createStringRequest(url,RequestMethod.GET);
        request.add("type",1);
        request.add("key",student.studentKey(getApplicationContext()));
        getRequestQueue().add(1, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                mLog.e("error","->"+response.get());
                try {
                    JSONObject object=new JSONObject(response.get());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        String data=object.getString("data");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length(); i++) {
                                JSONObject obj=array.getJSONObject(i);
                                String id=obj.getString("id");
                                String name=obj.getString("name");
                                checkObject check=new checkObject();
                                check.setId(id);
                                check.setName(name);
                                mdata.add(check);
                            }
                            handler.sendEmptyMessage(1111);
                        }
                    }else {
                        handler.sendEmptyMessage(8888);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                handler.sendEmptyMessage(9999);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }
}
