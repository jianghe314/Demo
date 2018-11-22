package com.kangzhan.student.Student.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.Test1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/3/30.
 */

public class Subject1ViewPagerAdapter extends PagerAdapter {
    private ArrayList<Test1> data;
    private Context mcontext;
    private List<View> viewItem;
    private int Grade=0;
    public Subject1ViewPagerAdapter(ArrayList<Test1> listdata, Context context, List<View> viewItems){
        data=listdata;
        mcontext=context;
        viewItem=viewItems;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    // 官方推荐这么写
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    // 移除页卡
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewItem.get(position));
    }

    // 这里返回View
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // 初始化viewholder
        final ViewHolder holder=new ViewHolder();
        // 取到每一个子项，进行赋值
        View convertView=viewItem.get(position);
        holder.problem_content= (TextView) convertView.findViewById(R.id.exam_questions);
        holder.problem_photo= (ImageView) convertView.findViewById(R.id.exam_questions_iv);
        //
        holder.line1= (LinearLayout) convertView.findViewById(R.id.exam_line1);
        holder.line2= (LinearLayout) convertView.findViewById(R.id.exam_line2);
        holder.line3= (LinearLayout) convertView.findViewById(R.id.exam_line3);
        holder.line4= (LinearLayout) convertView.findViewById(R.id.exam_line4);
        //
        holder.btn_a= (ImageButton) convertView.findViewById(R.id.choice_a);
        holder.btn_b= (ImageButton) convertView.findViewById(R.id.choice_b);
        holder.btn_c= (ImageButton) convertView.findViewById(R.id.choice_c);
        holder.btn_d= (ImageButton) convertView.findViewById(R.id.choice_d);
        holder.mult_btn= (Button) convertView.findViewById(R.id.mult_btn);
        holder.mult_btn.setVisibility(View.GONE);
        //
        holder.tv_a= (TextView) convertView.findViewById(R.id.tv_a);
        holder.tv_b= (TextView) convertView.findViewById(R.id.tv_b);
        holder.tv_c= (TextView) convertView.findViewById(R.id.tv_c);
        holder.tv_d= (TextView) convertView.findViewById(R.id.tv_d);

        //

        //
        final Test1 test1=data.get(position);
        //注意这里没有加占位图
        if(!test1.getPhotourl().equals("")){
            Glide.with(mcontext).load("http://app.kzxueche.com/"+test1.getPhotourl()).placeholder(R.drawable.placeholder).error(R.drawable.error).into(holder.problem_photo);
        }else {
            holder.problem_photo.setVisibility(View.GONE);
        }

        String[] content;
        //根据是单选还是判断来截取题目选项
        if(test1.getQuestion_type().equals("单选题")){
           // holder.prblem_type.setText("单选题");
            content=test1.getContent_body().split("\\$#");

            holder.problem_content.setText(content[0]);
            holder.tv_a.setText(content[1]);
            holder.tv_b.setText(content[2]);
            holder.tv_c.setText(content[3]);
            holder.tv_d.setText(content[4]);
            holder.btn_a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearBackground(holder.btn_a,holder.btn_b,holder.btn_c,holder.btn_d);
                    if(test1.getQuestion_remark().equals("A")){
                        setBackground(holder.btn_a,true,holder);
                        Grade++;
                        setGrade(Grade);
                    }else {
                        setBackground(holder.btn_a,false,holder);
                        showAnswer(test1.getQuestion_remark(),true,holder);


                    }

                }
            });
            holder.btn_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearBackground(holder.btn_a,holder.btn_b,holder.btn_c,holder.btn_d);
                    if(test1.getQuestion_remark().equals("B")){
                        setBackground(holder.btn_b,true,holder);
                        Grade++;
                        setGrade(Grade);
                    }else {
                        setBackground(holder.btn_b,false,holder);
                        showAnswer(test1.getQuestion_remark(),true,holder);
                    }
                }
            });
            holder.btn_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearBackground(holder.btn_a,holder.btn_b,holder.btn_c,holder.btn_d);
                    if(test1.getQuestion_remark().equals("C")){
                        setBackground(holder.btn_c,true,holder);
                        Grade++;
                        setGrade(Grade);
                    }else {
                        setBackground(holder.btn_c,false,holder);
                        showAnswer(test1.getQuestion_remark(),true,holder);
                    }
                }
            });
            holder.btn_d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearBackground(holder.btn_a,holder.btn_b,holder.btn_c,holder.btn_d);
                    if(test1.getQuestion_remark().equals("D")){
                        setBackground(holder.btn_d,true,holder);
                        Grade++;
                        setGrade(Grade);
                    }else {
                        setBackground(holder.btn_d,false,holder);
                        showAnswer(test1.getQuestion_remark(),true,holder);
                    }
                }
            });
        }else {
            //holder.prblem_type.setText("判断题");
            content=test1.getContent_body().split("\\$#");

            holder.problem_content.setText(content[0]);
            holder.tv_a.setText(content[1]);
            holder.tv_b.setText(content[2]);
            holder.line3.setVisibility(View.GONE);
            holder.line4.setVisibility(View.GONE);
            holder.btn_a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearBackground(holder.btn_a,holder.btn_b,holder.btn_c,holder.btn_d);
                    Log.i("判断题A-->",test1.getQuestion_remark());
                    if(test1.getQuestion_remark().equals("Y")){
                        setBackground(holder.btn_a,true,holder);
                        Grade++;
                        setGrade(Grade);
                    }else {
                        setBackground(holder.btn_a,false,holder);   //显示选择错误
                        showAnswer(test1.getQuestion_remark(),false,holder);    //显示正确答案
                    }
                }
            });
            holder.btn_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearBackground(holder.btn_a,holder.btn_b,holder.btn_c,holder.btn_d);
                    Log.i("判断题B-->",test1.getQuestion_remark());
                    if(test1.getQuestion_remark().equals("N")){
                        setBackground(holder.btn_b,true,holder);
                        Grade++;
                        setGrade(Grade);
                    }else {
                        setBackground(holder.btn_b,false,holder);
                        showAnswer(test1.getQuestion_remark(),false,holder);
                    }
                }
            });
        }


        container.addView(convertView);// 添加页卡
        return convertView; // 返回的是View对象进行显示
    }

    // ViewHolder控件容器
    class ViewHolder {
        TextView problem_content;
        ImageView problem_photo;
        LinearLayout line1,line2,line3,line4;
        ImageButton btn_a,btn_b,btn_c,btn_d;
        Button mult_btn;
        TextView tv_a,tv_b,tv_c,tv_d;
    }
    private void clearBackground(ImageButton btn1,ImageButton btn2,ImageButton btn3,ImageButton btn4){
        btn1.setBackgroundResource(R.drawable.choice0);
        btn2.setBackgroundResource(R.drawable.choice0);
        btn3.setBackgroundResource(R.drawable.choice0);
        btn4.setBackgroundResource(R.drawable.choice0);

    }
    //显示答案
    private void showAnswer(String right,boolean type,ViewHolder holder){
        //先判断什么题型
        if(type){
            switch (right){
                case "A":
                    setBackground(holder.btn_a,true,holder);
                    break;
                case "B":
                    setBackground(holder.btn_b,true,holder);
                    break;
                case "C":
                    setBackground(holder.btn_c,true,holder);
                    break;
                case "D":
                    setBackground(holder.btn_d,true,holder);
                    break;
                default:
                    break;
            }

        }else {
            switch (right){
                case "Y":
                    setBackground(holder.btn_a,true,holder);
                    break;
                case "N":
                    setBackground(holder.btn_b,true,holder);
                    break;
                default:
                    break;
            }
        }
    }
    private void setBackground(ImageButton btn,boolean tf,ViewHolder holder){
        if(tf){
            btn.setBackgroundResource(R.drawable.choice1);
            holder.btn_a.setEnabled(false);
            holder.btn_b.setEnabled(false);
            holder.btn_c.setEnabled(false);
            holder.btn_d.setEnabled(false);
        }else {
            btn.setBackgroundResource(R.drawable.wrong);
            holder.btn_a.setEnabled(false);
            holder.btn_b.setEnabled(false);
            holder.btn_c.setEnabled(false);
            holder.btn_d.setEnabled(false);
        }

    }
    private void setGrade(int grade){
        SharedPreferences sp=mcontext.getSharedPreferences("Grade",Context.MODE_PRIVATE);
        SharedPreferences.Editor edtor=sp.edit();
        edtor.putInt("grade",grade);
        edtor.apply();
    }




}
