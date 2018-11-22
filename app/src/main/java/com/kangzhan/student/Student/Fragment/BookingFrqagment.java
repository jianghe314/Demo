package com.kangzhan.student.Student.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.kangzhan.student.Student.Adapter.BookingRecordAdapter;
import com.kangzhan.student.Student.Booking.AmapNaviActivity;
import com.kangzhan.student.Student.Booking.SetLocationActivity;
import com.kangzhan.student.Student.Booking.Teacher_DetailActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.Interface.ItemCallPhone;
import com.kangzhan.student.Student.Interface.ItemOnNavi;
import com.kangzhan.student.Student.bean.BookingTeacher;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.com.BaseFragment;
import com.kangzhan.student.com.LazyFragment;
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

/**
 * Created by zhengxuan on 2017/3/27.
 */

public class BookingFrqagment extends Fragment implements View.OnClickListener{
    private View view;
    private TextView teacher_Num;
    private Button loadMore;
    private String mmsg,mphoneNum;
    private PullRecyclerView recycler;
    private BookingRecordAdapter adapter;
    private ArrayList<BookingTeacher> mdata=new ArrayList<>();
    //
    private RequestQueue requestQueue;
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            recycler.stopLoadMore();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 1111:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            recycler.stopLoadMore();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getActivity().getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 2222:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMessage.showMsg(getContext(),"后台没有给正确的地理位置哦");
                        }
                    });
                    break;
                case 9999:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            recycler.stopLoadMore();
                            recycler.setEmptyView(R.layout.item_no_internet);
                            showMessage.showMsg(getContext(),"网络连接异常，请检测网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.booking_layout,container,false);
            initView(view);
        }
        return view;
    }

    private void initView(View v) {
        requestQueue=NoHttp.newRequestQueue();
        gson=new Gson();
        teacher_Num= (TextView) v.findViewById(R.id.booking_number);
        loadMore= (Button) v.findViewById(R.id.get_more);
        loadMore.setOnClickListener(this);
        adapter=new BookingRecordAdapter(getContext(),R.layout.item_list_booking_adapte,mdata);
        recycler= (PullRecyclerView) v.findViewById(R.id.recorded_recycler);
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.item_no_data,null));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.setLayoutManager(new XLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initData();
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
                callPhone(phoneNum);
            }
        });
        //导航
        adapter.OnItemNavi(new ItemOnNavi() {
            @Override
            public void itemOnNavi(String lati, String longti) {
                if(lati==null||longti==null){
                    handler.sendEmptyMessage(2222);
                }else {
                    //开始导行
                    /*Intent mlocation=new Intent(getContext(),AmapNaviActivity.class);
                    mlocation.putExtra("Aim",new String[]{longti,lati});
                    startActivity(mlocation);*/

                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    if(isAvilible("com.baidu.BaiduMap")){
                        intent.setData(Uri.parse("baidumap://map/marker?location="+lati+","+longti+"&title=目的地"));
                        startActivity(intent);
                    }else if(isAvilible("com.autonavi.minimap")){
                        intent.setData(Uri.parse("http://uri.amap.com/marker?position="+longti+","+lati+"&name=目的地&src=康展学车&coordinate=gaode&callnative=1"));
                        startActivity(intent);
                    }else {
                        mToast.showText(getActivity().getApplicationContext(),"您未安装百度或高德地图");
                    }

                }
            }
        });
    }

    private void initData() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentBookingList(), RequestMethod.GET);
        request.add("key",student.studentKey(getContext()));
        mLog.e("key",student.studentKey(getContext()));
        requestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","-->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if( object.getString("code").equals("200")){
                        JSONArray array=new JSONArray(object.getString("data"));
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length() ; i++) {
                                JSONObject ob=array.getJSONObject(i);
                                BookingTeacher teacher=gson.fromJson(ob.toString(),BookingTeacher.class);
                                mdata.add(teacher);
                            }
                        }
                        handler.sendEmptyMessage(0000);
                    }else {
                        mdata.clear();
                        handler.sendEmptyMessage(1111);
                    }
                    teacher_Num.setText(mdata.size()+"");
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

    private void callPhone(String phoneNum) {
        //>=23
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            int checkCallPhone= ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);
            if(checkCallPhone!= PackageManager.PERMISSION_GRANTED){
                //没有授权,请求授权
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},1);
            }else {
                //授权
                mcallPhone(phoneNum);
            }
        }else {
            mcallPhone(phoneNum);
        }
    }
    //权限申请回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mcallPhone(mphoneNum);
                }else {
                    mToast.showText(getContext(),"没有权限拨打电话！");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void mcallPhone(String phoneNum) {
        Intent call=new Intent();
        call.setAction(Intent.ACTION_CALL);
        call.setData(Uri.parse("tel:"+phoneNum));
        startActivity(call);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.get_more){
            Intent loadMore=new Intent(getContext(),SetLocationActivity.class);
            startActivity(loadMore);
        }
    }

    public boolean isAvilible(String packageName){
        PackageManager packageManager=getContext().getPackageManager();
        List<PackageInfo> packageInfos=packageManager.getInstalledPackages(0);
        List<String> packageNames=new ArrayList<>();
        if(packageInfos!=null){
            for (int i = 0; i <packageInfos.size() ; i++) {
                packageNames.add(packageInfos.get(i).packageName);
            }
        }
        return packageNames.contains(packageName);
    }


    @Override
    public void onDestroy() {
        requestQueue.cancelAll();
        super.onDestroy();
    }

}
