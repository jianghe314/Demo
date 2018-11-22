package com.kangzhan.student.Student.person_data_activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.BookingOrderAdapter;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.Interface.ItemCallPhone;
import com.kangzhan.student.Student.bean.Advise;
import com.kangzhan.student.Student.bean.BookingOrder;
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

public class BookingRecordActivity extends BaseActivity {
    private PullRecyclerView recycler;
    private TextView record_number,record_spend;
    private BookingOrderAdapter adapter;
    private ArrayList<BookingOrder> mdata=new ArrayList<>();
    private Gson gson;
    private int mposition,sum_price;
    private String count,mmsg,mphoneNum;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            adapter.notifyDataSetChanged();
                            record_number.setText(mdata.size()+"");
                            record_spend.setText(sum_price+"");
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(BookingRecordActivity.this,"取消中...");
                        }
                    });
                    break;
                case 3344:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),"取消成功！");
                        }
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendRequest();
                        }
                    }).start();
                    break;
                case 4444:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            AlertDialog.Builder builder=new AlertDialog.Builder(BookingRecordActivity.this);
                            builder.setMessage(mmsg);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                            adapter.notifyDataSetChanged();
                            record_number.setText(mdata.size()+"");
                            record_spend.setText(sum_price);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            recycler.stopRefresh();
                            showMessage.showMsg(BookingRecordActivity.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_booking_record);
        Toolbar toolbar= (Toolbar) findViewById(R.id.booking_order_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        iniView();
    }

    private void iniView() {
        record_number= (TextView) findViewById(R.id.record_number);
        record_spend= (TextView) findViewById(R.id.record_spend);
        recycler= (PullRecyclerView) findViewById(R.id.booking_record);
        adapter=new BookingOrderAdapter(BookingRecordActivity.this,R.layout.item_list_bookingrecord_adapter,mdata);
        adapter.SetOnLongClick(new BookingOrderAdapter.msetOnLongClickListener() {
            @Override
            public void OnLongClick(final int position, final BookingOrder item) {
                mposition=position;
                AlertDialog.Builder builder=new AlertDialog.Builder(BookingRecordActivity.this);
                builder.setItems(new String[]{"取消"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            if(item.getIscancel().equals("1")){
                                handler.sendEmptyMessage(3333);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CancelOrder(item.getId());
                                    }
                                }).start();
                            }else {
                                mToast.showText(getApplicationContext(),"预约课程需要提前一天取消！");
                            }
                        }
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.setLayoutManager(new XLinearLayoutManager(BookingRecordActivity.this, LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new DividerItemDecoration(BookingRecordActivity.this,DividerItemDecoration.VERTICAL_LIST));
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest();
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {

                recycler.stopLoadMore();
            }
        });
        recycler.postRefreshing();
        adapter.CallPhone(new ItemCallPhone() {
            @Override
            public void itemCallPhone(String phoneNum) {
                mphoneNum=phoneNum;
                callPhone();
            }
        });
    }


    private void CancelOrder(String id) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentCancelOrder(),RequestMethod.POST);
        request.add("key",student.studentKey(getApplicationContext()));
        request.add("order_id",id);
        request.add("cancel_type","10");
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
                    if(object.getString("code").equals("200")){
                        handler.sendEmptyMessage(3344);
                    }else {
                        handler.sendEmptyMessage(4444);
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

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentBookingOrder(), RequestMethod.GET);
        request.add("key",student.studentKey(BookingRecordActivity.this));
        request.add("stu_id",student.studentId(BookingRecordActivity.this));
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        count=object.getString("count");
                        sum_price=object.getInt("totalPrice");
                        String data=object.getString("data");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj=array.getJSONObject(i);
                                BookingOrder order=gson.fromJson(obj.toString(),BookingOrder.class);
                                mdata.add(order);
                            }
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
                handler.sendEmptyMessage(9999);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void callPhone() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int checkPerssion= ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if(checkPerssion!= PackageManager.PERMISSION_GRANTED){
                //没有授权
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
            }else {
                call();
            }
        }else {
            call();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED ){
                    call();
                }else {
                    mToast.showText(getApplicationContext(),"没有权限拨打电话！");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void call() {
        Intent itent=new Intent();
        itent.setAction(Intent.ACTION_CALL);
        itent.setData(Uri.parse("tel:"+mphoneNum));
        startActivity(itent);
    }

}
