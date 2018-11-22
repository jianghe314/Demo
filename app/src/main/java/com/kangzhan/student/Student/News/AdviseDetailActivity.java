package com.kangzhan.student.Student.News;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.bean.AdviseDetail;
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

public class AdviseDetailActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private String id;
    private CardView cardView;
    private TextView problem, deal, title;
    private Button addProblem;
    private AdviseDetail detail;
    private Gson gson;
    //
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(AdviseDetailActivity.this, "加载中...");
                        }
                    });
                    getData();
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                        }
                    });
                    try {
                        title.setText(detail.getSummary());
                        problem.setText(detail.getContent());
                        if (!detail.getSchopinion().equals("")) {
                            cardView.setVisibility(View.VISIBLE);
                            deal.setText(detail.getSchopinion());
                        }
                        if (!detail.getDepaopinion().equals("")) {
                            cardView.setVisibility(View.VISIBLE);
                            deal.setText(detail.getDepaopinion());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),"加载失败，请稍后再试！");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(AdviseDetailActivity.this, "加载失败，请稍后再试");
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advise_detail);
        Intent getIntent = getIntent();
        id = getIntent.getStringExtra("id");
        toolbar = (Toolbar) findViewById(R.id.student_adviseDetail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        gson = new Gson();
        initView();
    }

    private void initView() {
        cardView = (CardView) findViewById(R.id.student_adviseDetail_card2);
        problem = (TextView) findViewById(R.id.advice_problem_content);
        title = (TextView) findViewById(R.id.notice_receiver);
        deal = (TextView) findViewById(R.id.advise_detail_deal);
        addProblem = (Button) findViewById(R.id.student_addProblem);
        addProblem.setOnClickListener(this);
        handler.sendEmptyMessage(0000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.student_addProblem:
                String[] bb = {"教练", "驾校"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(AdviseDetailActivity.this);
                dialog.setTitle("选择投诉对象");
                dialog.setItems(bb, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent teacher = new Intent(AdviseDetailActivity.this, AddAdviseActivity.class);
                            teacher.putExtra("who", which);
                            startActivity(teacher);
                            dialog.dismiss();
                        } else if (which == 1) {
                            Intent teacher = new Intent(AdviseDetailActivity.this, AddAdviseActivity.class);
                            teacher.putExtra("who", which);
                            startActivity(teacher);
                            dialog.dismiss();
                        }
                    }
                }).show();
                break;
            default:
                break;
        }
    }

    private void getData() {
        final Request<JSONObject> request = NoHttp.createJsonObjectRequest(student.studentMsgAdviseDetail(), RequestMethod.GET);
        request.add("key", student.studentKey(AdviseDetailActivity.this));
        request.add("complaint_id", id);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object = new JSONObject(response.get().toString());
                    String data = object.getString("data");
                    if (object.getString("code").equals("200")) {
                        JSONArray array = new JSONArray(data);
                        JSONObject obj = array.getJSONObject(0);
                        detail = gson.fromJson(obj.toString(), AdviseDetail.class);
                        handler.sendEmptyMessage(1111);
                    } else {
                        handler.sendEmptyMessage(2222);
                    }
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

}
