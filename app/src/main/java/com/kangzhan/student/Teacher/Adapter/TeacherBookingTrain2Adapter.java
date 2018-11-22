package com.kangzhan.student.Teacher.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.Booking.Teacher_student_detail;
import com.kangzhan.student.Teacher.bean.TeacherBookingTrain1;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/4/12.
 */

public class TeacherBookingTrain2Adapter extends BaseRecyclerAdapter<TeacherBookingTrain1> {
    private Context context;
    private ArrayList<TeacherBookingTrain1> data;
    private RequestQueue requstQueue;
    private String mmsg;
    private int mposition;
    private Activity activity;
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
    public TeacherBookingTrain2Adapter(Context context, int layoutResId, ArrayList<TeacherBookingTrain1> data,Activity activity) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
        this.activity=activity;
    }

    @Override
    protected void convert(BaseViewHolder holder, final TeacherBookingTrain1 item) {
        mposition=holder.getAdapterPosition();
        View colorLine= holder.getView().findViewById(R.id.item_teacher_booking2_colorLine);
        TextView lesson= (TextView)holder.getView().findViewById(R.id.item_teacher_booking2_lesson);
        TextView name= (TextView)holder.getView().findViewById(R.id.item_teacher_booking2_name);
        TextView queke= (TextView) holder.getView().findViewById(R.id.item_teacher_booking2_queke);
        TextView time= (TextView)holder.getView().findViewById(R.id.item_teacher_booking2_time);
        TextView  price= (TextView)holder.getView().findViewById(R.id.item_teacher_booking2_price);
        TextView carLabel= (TextView)holder.getView().findViewById(R.id.item_teacher_booking2_carLabel);
        TextView startTrain= (TextView)holder.getView().findViewById(R.id.item_teacher_booking2_startTrain);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_teacher_booking2_sex);
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.item_teacher_booking2_header);
        //
        if(holder.getAdapterPosition()%2==0){
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.text_background_color_aqua));
        }else if(holder.getAdapterPosition()%2==1){
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_orange));
        }else {
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
        if(data.size()>0){
            lesson.setText(item.getStage_name());
            if(item.getStudent_sex().equals("女")){
                sex.setImageResource(R.mipmap.girl);
            }else {
                sex.setImageResource(R.mipmap.boy);
            }
            Glide.with(context).load(item.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
            name.setText(item.getStudent_name());
            time.setText(item.getTime_name());
            price.setText(item.getAmount()+"元");
            carLabel.setText(item.getStudent_traintype()+" "+item.getCar_name());
            //
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
                    builder.setCancelable(true);
                    builder.create().show();
                }
            });
            queke.setOnClickListener(new View.OnClickListener() {
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
        }



    }

    private void sendRequest(TeacherBookingTrain1 item) {
        requstQueue=NoHttp.newRequestQueue();
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherAbsence(),RequestMethod.GET);
        request.add("key",teacher.teacherKey(context));
        request.add("id",item.getId());
        request.add("stu_id",item.getStu_id());
        request.add("subj_id",item.getSubj_id());
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

                    Message msg=new Message();
                    msg.what=1111;
                    handler.sendMessage(msg);
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

    private String  getImagerURl(TeacherBookingTrain1 item) {
        String url=teacher.teacherStartTrain2DCode()+"?key="+teacher.teacherKey(context)+"&id="+item.getId()+"&stu_id="+item.getStu_id()+"&coach_id="+item.getCoach_id();
        return url;
    }


}
