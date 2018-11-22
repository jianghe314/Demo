package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.Teacher.Booking.BookingTrain1;
import com.kangzhan.student.Teacher.Booking.Teacher_student_detail;
import com.kangzhan.student.Teacher.bean.TeacherBookingTrain1;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/4/12.
 */

public class TeacherBookingTrain1Adapter extends BaseRecyclerAdapter<TeacherBookingTrain1> {
    private Context context;
    private TakePhoto photo;
    private ArrayList<TeacherBookingTrain1> data;
    public TeacherBookingTrain1Adapter(Context context, int layoutResId, ArrayList<TeacherBookingTrain1> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }
    public void takePhoto(TakePhoto takePhoto){
        this.photo=takePhoto;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final TeacherBookingTrain1 item) {
        View colorLine= holder.getView().findViewById(R.id.item_teacher_booking1_colorLine);
        TextView lesson= (TextView)holder.getView().findViewById(R.id.item_teacher_booking1_lesson);
        TextView name= (TextView)holder.getView().findViewById(R.id.item_teacher_booking1_name);
        TextView time= (TextView)holder.getView().findViewById(R.id.item_teacher_booking1_time);
        final ImageView takePhoto= (ImageView) holder.getView().findViewById(R.id.item_teacher_booking1_takePhoto);
        TextView  price= (TextView)holder.getView().findViewById(R.id.item_teacher_booking1_price);
        TextView carLabel= (TextView)holder.getView().findViewById(R.id.item_teacher_booking1_carLabel);
        TextView endTrain= (TextView)holder.getView().findViewById(R.id.teacher_train_order_endTrain);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_teacher_booking1_sex);
        final CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.item_teacher_booking1_header);
        //
        if(holder.getAdapterPosition()%2==0){
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_orange));
        }else if(holder.getAdapterPosition()%2==1){
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.text_background_color_aqua));
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
            endTrain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    LayoutInflater inflater=LayoutInflater.from(context);
                    View view=inflater.inflate(R.layout.teacher_booking_dialog,null);
                    builder.setView(view);
                    builder.setTitle("扫描二维码结束培训");
                    ImageView iv= (ImageView) view.findViewById(R.id.teacher_booking_2DCode);
                    Glide.with(context).load(getImagerURl(item)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.error).placeholder(R.drawable.banner).into(iv);
                    builder.setCancelable(true);
                    builder.create().show();
                }
            });
        }

        //拍照
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photo!=null){
                    photo.takePhoto(takePhoto,holder.getAdapterPosition());
                }
            }
        });
    }


    //http://192.168.0.112/studentapi/Appoint/startTrainqrcode?key=17kzc149437941058595083&id=1&stu_id=1&coach_id=1
    private String  getImagerURl(TeacherBookingTrain1 item) {
        String url= teacher.teacherEndTrain2DCode()+"?key="+teacher.teacherKey(context)+"&id="+item.getId()+"&stu_id="+item.getStu_id()+"&subj_id="+item.getSubj_id()+"&coach_id="+item.getCoach_id();
        return url;
    }
    public interface TakePhoto{
        void takePhoto(ImageView iv,int position);
    }
}
