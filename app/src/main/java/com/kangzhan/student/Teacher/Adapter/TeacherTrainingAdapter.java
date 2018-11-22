package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
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
import com.kangzhan.student.Teacher.bean.TeacherTrainOrder;
import com.kangzhan.student.Teacher.bean.TeacherTrainRecord;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/5/17.
 */

public class TeacherTrainingAdapter extends BaseRecyclerAdapter<TeacherTrainRecord> {
    private Context context;
    private ArrayList<TeacherTrainRecord> data=new ArrayList<>();
    public TeacherTrainingAdapter(Context context, int layoutResId, ArrayList<TeacherTrainRecord> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final TeacherTrainRecord item) {
        View colorLine=holder.getView().findViewById(R.id.teacher_training_colorLine);
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.teacher_training_header);
        TextView lesson= (TextView) holder.getView().findViewById(R.id.teacher_training_isId);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.teacher_training_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.teacher_training_name);
        TextView pickup= (TextView) holder.getView().findViewById(R.id.teacher_train_order_pickUp);
        TextView price= (TextView) holder.getView().findViewById(R.id.teacher_training_price);
        TextView date= (TextView) holder.getView().findViewById(R.id.teacher_training_time);
        TextView address= (TextView) holder.getView().findViewById(R.id.teacher_training_address);
        TextView endTrain= (TextView) holder.getView().findViewById(R.id.teacher_training_startTrain);
        //赋值数据
        if(holder.getAdapterPosition()%2==0){
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_orange));
        }else if(holder.getAdapterPosition()%2==1){
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else {
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.text_background_color_aqua));
        }
        Glide.with(context).load(item.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
        lesson.setText(item.getDetail());
        if(item.getSex().equals("2")){
            sex.setImageResource(R.mipmap.girl);
        }else {
            sex.setImageResource(R.mipmap.boy);
        }
        name.setText(item.getStuname());
        pickup.setText(item.getShuttle_name());
        price.setText("￥"+item.getPrice());
        date.setText(item.getStart_time()+"-"+item.getEnd_time());
        address.setText(item.getAddress());
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


    private String  getImagerURl(TeacherTrainRecord item) {
        String url= teacher.teacherTrainEnd2DCode()+"?key="+teacher.teacherKey(context)+"&task_id="+item.getTask_id()+"&stu_id="+item.getStu_id()+"&coach_id="+item.getCoach_id();
        return url;
    }
}
