package com.kangzhan.student.Student.Train;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.TrainBookingAdapter;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.TrainBooking;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
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
import java.util.List;

public class Train_BookingActivity extends BaseActivity {
    private TextView booking_Num,booking_has,booking_price_Sum;
    private RecyclerView recyclerView;
    private TrainBookingAdapter adapter;
    private String Num,has,price;
    private String mmsg;
    private int mpostion;
    private ArrayList<TrainBooking> mdata=new ArrayList<>();
    private ArrayList<TrainBooking> dataN=new ArrayList<>();
    private ArrayList<TrainBooking> dataY=new ArrayList<>();
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress.showProgress(Train_BookingActivity.this,"加载中...");
                    }
                });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            adapter.notifyDataSetChanged();
                            booking_Num.setText(Num);
                            booking_has.setText(has);
                            booking_price_Sum.setText(price);
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Train_BookingActivity.this,"取消中...");
                        }
                    });
                    break;
                case 4444:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            AlertDialog.Builder builder=new AlertDialog.Builder(Train_BookingActivity.this);
                            builder.setMessage(mmsg);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            sendRequest();
                                        }
                                    }).start();
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(Train_BookingActivity.this,"网络连接异常，请检测网络连接");
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
        setContentView(R.layout.activity_train__booking);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_tain_booking_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        booking_Num= (TextView) findViewById(R.id.booking_Num);
        booking_has= (TextView) findViewById(R.id.booking_has);
        booking_price_Sum= (TextView) findViewById(R.id.booking_price_Sum);
        recyclerView= (RecyclerView) findViewById(R.id.train_booking_list);
        adapter=new TrainBookingAdapter(Train_BookingActivity.this,mdata);
        adapter.setCancelOrder(new TrainBookingAdapter.setCancelOrder() {
            @Override
            public void SetCancelOrder(final TrainBooking item, final int postion) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Train_BookingActivity.this);
                builder.setItems(new String[]{"取消"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0) {
                            //删除该订单
                            if (item.getIs_cancel().equals("1")) {
                                handler.sendEmptyMessage(3333);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        cancelOrder(item.getId());
                                    }
                                }).start();
                            }
                        }else {
                            mToast.showText(getApplicationContext(),"已接单的订单需要提前两天取消哦");
                        }
                    }
                });
                builder.create().show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(Train_BookingActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(Train_BookingActivity.this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest();
            }
        }).start();
    }

    private void cancelOrder(String id) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentTrainCancelOrder(),RequestMethod.GET);
        request.add("key",student.studentKey(getApplicationContext()));
        request.add("ids",id);
        getRequestQueue().add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    handler.sendEmptyMessage(4444);
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

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentTrainOrder(), RequestMethod.GET);
        request.add("key",student.studentKey(Train_BookingActivity.this));
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    mmsg=object.getString("msg");
                    if(code.equals("200")){
                        Num=object.getString("totalOrder");
                        has=object.getString("ordersTaking");
                        price=object.getString("totalPrice");
                        String data1=object.getString("takingList");
                        String data2=object.getString("notakingList");
                        JSONArray array1=new JSONArray(data1);
                        if(array1.length()>0){
                            dataY.clear();
                            for (int i = 0; i <array1.length(); i++) {
                                TrainBooking book1=gson.fromJson(array1.getJSONObject(i).toString(),TrainBooking.class);
                                dataY.add(book1);
                            }

                        }else {
                            dataY.clear();
                        }
                        JSONArray array2=new JSONArray(data2);
                        if(array2.length()>0){
                            dataN.clear();
                            for (int i = 0; i <array2.length(); i++) {
                                TrainBooking book2=gson.fromJson(array2.getJSONObject(i).toString(),TrainBooking.class);
                                dataN.add(book2);
                            }
                        }else {
                            dataN.clear();
                        }
                        for (int i = 0; i < dataN.size(); i++) {
                            mdata.add(dataN.get(i));
                        }
                        for (int i = 0; i <dataY.size(); i++) {
                            mdata.add(dataY.get(i));
                        }
                        handler.sendEmptyMessage(1111);
                    }else {
                        handler.sendEmptyMessage(2222);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                Message msg=new Message();
                msg.what=9999;
                handler.sendMessage(msg);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

}
