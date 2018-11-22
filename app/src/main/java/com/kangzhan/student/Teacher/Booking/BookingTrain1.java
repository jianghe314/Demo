package com.kangzhan.student.Teacher.Booking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.Teacher.Adapter.TeacherBookingTrain1Adapter;
import com.kangzhan.student.Teacher.bean.TeacherBookingTrain1;
import com.kangzhan.student.com.LazyFragment;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.kangzhan.student.mUI.DividerGridItemDecoration;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kangzhan011 on 2017/5/9.
 */

public class BookingTrain1 extends Fragment{
    private RequestQueue requestQueue;
    private Gson gson;
    private Uri imageUri;
    private ImageView photo;
    private TextView person,hour;
    private final int TAKE_PHOTO=1;
    private PullRecyclerView recycler;
    private View view;
    private String mmsg;
    private TeacherBookingTrain1Adapter adapter;
    private ArrayList<TeacherBookingTrain1> mdata1=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            person.setText(mdata1.size()+"");
                            //hour.setText(100+"");
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getActivity().getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 9999:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            mToast.showText(getActivity().getApplicationContext(),"加载失败，请稍后再试");
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       if(view==null){
           view=inflater.inflate(R.layout.teacher_booking_train1,container,false);
           requestQueue=NoHttp.newRequestQueue();
           gson=new Gson();
           initView(view);

       }
       return view;
    }

    private void initView(View v) {
        person= (TextView) v.findViewById(R.id.teacher_booking_train1_sum);
        //hour= (TextView) v.findViewById(R.id.teacher_booking_train1_hours);
        adapter=new TeacherBookingTrain1Adapter(getContext(),R.layout.item_teacher_booking,mdata1);
        adapter.takePhoto(new TeacherBookingTrain1Adapter.TakePhoto() {
            @Override
            public void takePhoto(ImageView iv, int position) {
                photo=iv;
                File outputImage=new File(getContext().getExternalCacheDir(),"takePhoto.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                //7.0权限处理
                if(Build.VERSION.SDK_INT >= 24){
                    imageUri= FileProvider.getUriForFile(getContext(),"com.kangzhan.student.fileprovider",outputImage);
                }else {
                    imageUri= Uri.fromFile(outputImage);
                }
                //拍照
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });
        //
        recycler= (PullRecyclerView) v.findViewById(R.id.teacher_booking_train1_recycler);
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.item_no_data,null));
        recycler.setLayoutManager(new XLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            // 模拟下拉刷新网络请求
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest();
                    }
                }).start();

            }
            // 模拟上拉加载更多网络请求
            @Override
            public void onLoadMore() {
                recycler.stopLoadMore();
            }
        });
        recycler.postRefreshing();
    }

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherBooking1(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getContext().getApplicationContext()));
        request.add("status",20);
        requestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response1","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        JSONArray array=new JSONArray(object.getString("data"));
                        if(array.length()>0){
                            mdata1.clear();
                            for (int i = 0; i <array.length(); i++) {
                                JSONObject obj=array.getJSONObject(i);
                                TeacherBookingTrain1 train=gson.fromJson(obj.toString(),TeacherBookingTrain1.class);
                                mdata1.add(train);
                            }
                            handler.sendEmptyMessage(1111);
                        }
                    }else {
                        mdata1.clear();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    try {
                        BitmapFactory.Options options=new BitmapFactory.Options();
                        options.inJustDecodeBounds=true;
                        BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageUri),null,options);
                        int width=options.outWidth;
                        int height=options.outHeight;
                        int be=2;
                        int minLen=Math.min(width,height);
                        if(minLen>100){
                            float ratio=minLen/100.0f;
                            be= (int) ratio;
                        }
                        options.inJustDecodeBounds=false;
                        options.inSampleSize=be;
                        Bitmap bm=BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageUri),null,options);
                        photo.setImageBitmap(bm);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll();
            requestQueue.stop();
        }
    }
}
