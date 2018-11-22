package com.kangzhan.student.Teacher.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.bean.TeacherBookingTrain1;
import com.kangzhan.student.Teacher.bean.TeacherTrainOrder;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by kangzhan011 on 2017/4/13.
 */

public class TeacherTrainOrderAdapter extends BaseRecyclerAdapter<TeacherTrainOrder>{
    private Context context;
    private ArrayList<TeacherTrainOrder> data;
    private Activity activity;
    private String mmsg;
    private int mposition;
    private RequestQueue requstQueue;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(context,"同步中...");
                        }
                    });
                    break;
                case 1111:
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            AlertDialog.Builder builder=new AlertDialog.Builder(context);
                            builder.setMessage(mmsg);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    data.remove(mposition);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    });
                    break;
                case 9999:
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            AlertDialog.Builder builder=new AlertDialog.Builder(context);
                            builder.setMessage("网络连接异常，请检测网络");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };
    public TeacherTrainOrderAdapter(Context context, int layoutResId, ArrayList<TeacherTrainOrder> data,Activity activity) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
        this.activity=activity;
        requstQueue= NoHttp.newRequestQueue();
    }

    @Override
    protected void convert(final BaseViewHolder holder, final TeacherTrainOrder item) {
        mposition=holder.getAdapterPosition();
        View colorline=holder.getView().findViewById(R.id.teacher_train_order_colorLine);
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.teacher_train_order_header);
        TextView lesson= (TextView) holder.getView().findViewById(R.id.teacher_train_order_isId);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.teacher_train_order_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.teacher_train_order_name);
        TextView isPickUp= (TextView) holder.getView().findViewById(R.id.teacher_train_order_pickUp);
        TextView date= (TextView) holder.getView().findViewById(R.id.teacher_train_order_time);
        TextView address= (TextView) holder.getView().findViewById(R.id.teacher_train_order_address);
        TextView price= (TextView) holder.getView().findViewById(R.id.teacher_train_order_price);
        TextView absence= (TextView) holder.getView().findViewById(R.id.teacher_train_order_absence);
        TextView startTrain= (TextView) holder.getView().findViewById(R.id.teacher_train_order_startTrain);
        //填数据
        if(holder.getAdapterPosition()%3==0){
            colorline.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_orange));
        }else if(holder.getAdapterPosition()%3==1){
            colorline.setBackgroundColor(ContextCompat.getColor(context,R.color.text_background_color_aqua));
        }else {
            colorline.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
        Glide .with(context).load(item.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
        lesson.setText(item.getDetail());
        if(item.getSex().equals("女")){
            sex.setImageResource(R.mipmap.girl);
        }else {
            sex.setImageResource(R.mipmap.boy);
        }
        name.setText(item.getStuname());
        isPickUp.setText(item.getShuttle());
        date.setText(item.getStart_time()+"-"+item.getEnd_time());
        address.setText(item.getAddress());
        price.setText(item.getPrice()+"元");
        absence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("确认缺课吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.sendEmptyMessage(0000);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sendRequest(item);
                            }
                        }).start();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);
                builder.create().show();
            }
        });
        startTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                LayoutInflater inflater=LayoutInflater.from(context);
                View view=inflater.inflate(R.layout.teacher_booking_dialog,null);
                builder.setView(view);
                builder.setTitle("扫描二维码开始培训");
                ImageView iv= (ImageView) view.findViewById(R.id.teacher_booking_2DCode);
                Glide.with(context).load(getImagerURl(item)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.error).placeholder(R.drawable.banner).into(iv);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
                builder.setCancelable(true);
                builder.create().show();
            }
        });
    }
    private void sendRequest(TeacherTrainOrder item) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherTrainAbsence2DCode(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(context));
        request.add("task_id",item.getTask_id());
        request.add("stu_id",item.getStu_id());
        request.add("coach_id",item.getCoach_id());
        requstQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    handler.sendEmptyMessage(1111);
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

    //http://192.168.0.112/studentapi/Appoint/startTrainqrcode?key=17kzc149437941058595083&id=1&stu_id=1&coach_id=1
    private String  getImagerURl(TeacherTrainOrder item) {
        String url= teacher.teacherTrainStart2DCode()+"?key="+teacher.teacherKey(context)+"&task_id="+item.getTask_id()+"&stu_id="+item.getStu_id()+"&coach_id="+item.getCoach_id();
        return url;
    }
    public interface RefreshList{
        void Refresh();
    }

}
