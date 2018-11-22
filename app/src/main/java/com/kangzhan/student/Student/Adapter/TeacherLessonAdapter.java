package com.kangzhan.student.Student.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.TeacherLesson;
import com.kangzhan.student.Teacher.Adapter.TeacherClassAdapter;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/5/5.
 */

public class TeacherLessonAdapter extends RecyclerView.Adapter<TeacherLessonAdapter.mViewHolder>{
    private Context context;
    private ArrayList<TeacherLesson> data;
    private Activity activity;
    private float screenWidth;
    public TeacherLessonAdapter(Context context, ArrayList<TeacherLesson> data,Activity activity) {
        this.context=context;
        this.data=data;
        this.activity=activity;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_list_teacher_lesson,parent,false);
        DisplayMetrics display=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(display);
        screenWidth=display.widthPixels;
        GridLayoutManager.LayoutParams rl= (GridLayoutManager.LayoutParams) view.getLayoutParams();
        float width=((screenWidth/3)-12);
        //Log.e("width","->"+width);
        rl.width= (int) width;
        rl.height= (int) width;
        view.setLayoutParams(rl);
        mViewHolder viewHolder=new mViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final mViewHolder holder, int position) {
        final TeacherLesson lesson=data.get(position);
        holder.time.setText(lesson.getStarttime()+"-"+lesson.getEndtime());
        holder.price.setText(lesson.getStage()+lesson.getPerdritype()+"/"+lesson.getPrice()+"元");
        if("1".equals(lesson.getIs_gray())){
            //不可约
            holder.carType.setText(lesson.getMsg());
            holder.itemView.setBackgroundResource(R.drawable.is_gray);
        }else {
            //可约
            holder.carType.setText("已约"+lesson.getNumber()+"人 "+"可约"+lesson.getMax_stu()+"人");
            holder.itemView.setBackgroundResource(R.drawable.text_background5);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lesson.getIs_gray().equals("0")){
                    //可以点击
                    if(lesson.isClick()){
                        lesson.setClick(false);
                        holder.itemView.setBackgroundResource(R.drawable.text_background5);
                    }else {
                        lesson.setClick(true);
                        holder.itemView.setBackgroundResource(R.drawable.choice_lesson);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public  class mViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        TextView price;
        TextView carType;
        View itemView;
        private mViewHolder(View view) {
            super(view);
            itemView=view;
            time= (TextView) view.findViewById(R.id.item_teacher_lesson_time);
            price= (TextView) view.findViewById(R.id.item_teacher_lesson_price);
            carType= (TextView) view.findViewById(R.id.item_teacher_lesson_carType);
        }
    }

}
