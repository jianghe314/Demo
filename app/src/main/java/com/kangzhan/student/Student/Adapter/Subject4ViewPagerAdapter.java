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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.Test4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/3/31.
 */

public class Subject4ViewPagerAdapter extends PagerAdapter {

    private ArrayList<Test4> data;
    private Context mcontext;
    private List<View> viewItem;
    private int Grade=0;

    public Subject4ViewPagerAdapter(ArrayList<Test4> listdata, Context context, List<View> viewItems){
        data=listdata;
        mcontext=context;
        viewItem=viewItems;
    }
    @Override
    public int getCount() {
        return data.size();
    }


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

        final Test4 test4=data.get(position);
        //注意这里没有加占位图
        if(!test4.getPhotourl().equals("")){
            //192.168.0.112
            Glide.with(mcontext).load("http://app.kzxueche.com/"+test4.getPhotourl()).placeholder(R.drawable.placeholder).error(R.drawable.error).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.problem_photo);
            //Log.e("gif->","http://192.168.0.112/"+test4.getPhotourl());
        }else {
            holder.problem_photo.setVisibility(View.GONE);
        }
        String[] content;
        //根据是单选还是判断来截取题目选项
        if(test4.getQuestion_type().equals("单选题")){
            content=test4.getContent_body().split("\\$#");

            holder.problem_content.setText(content[0]);
            holder.tv_a.setText(content[1]);
            holder.tv_b.setText(content[2]);
            holder.tv_c.setText(content[3]);
            holder.tv_d.setText(content[4]);
            holder.btn_a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearBackground(holder.btn_a,holder.btn_b,holder.btn_c,holder.btn_d);
                    if(test4.getQuestion_remark().equals("A")){
                        setBackground(holder.btn_a,true,holder);
                        Grade++;
                        setGrade(Grade);
                    }else {
                        setBackground(holder.btn_a,false,holder);
                        showAnswer(test4.getQuestion_remark(),true,holder);


                    }

                }
            });
            holder.btn_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearBackground(holder.btn_a,holder.btn_b,holder.btn_c,holder.btn_d);
                    if(test4.getQuestion_remark().equals("B")){
                        setBackground(holder.btn_b,true,holder);
                        Grade++;
                        setGrade(Grade);
                    }else {
                        setBackground(holder.btn_b,false,holder);
                        showAnswer(test4.getQuestion_remark(),true,holder);
                    }
                }
            });
            holder.btn_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearBackground(holder.btn_a,holder.btn_b,holder.btn_c,holder.btn_d);
                    if(test4.getQuestion_remark().equals("C")){
                        setBackground(holder.btn_c,true,holder);
                        Grade++;
                        setGrade(Grade);
                    }else {
                        setBackground(holder.btn_c,false,holder);
                        showAnswer(test4.getQuestion_remark(),true,holder);
                    }
                }
            });
            holder.btn_d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearBackground(holder.btn_a,holder.btn_b,holder.btn_c,holder.btn_d);
                    if(test4.getQuestion_remark().equals("D")){
                        setBackground(holder.btn_d,true,holder);
                        Grade++;
                        setGrade(Grade);
                    }else {
                        setBackground(holder.btn_d,false,holder);
                        showAnswer(test4.getQuestion_remark(),true,holder);
                    }
                }
            });
        }else if(test4.getQuestion_type().equals("判断题")){
            //Log.e("判断题--》",test4.getContent_body()+"--"+test4.getQuestion_remark());

            content=test4.getContent_body().split("\\$#");

            holder.problem_content.setText(content[0]);
            holder.tv_a.setText(content[1]);
            holder.tv_b.setText(content[2]);
            holder.line3.setVisibility(View.GONE);
            holder.line4.setVisibility(View.GONE);
            holder.btn_a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearBackground(holder.btn_a,holder.btn_b,holder.btn_c,holder.btn_d);
                    if(test4.getQuestion_remark().equals("Y")){
                        setBackground(holder.btn_a,true,holder);
                        Grade++;
                        setGrade(Grade);
                    }else {
                        setBackground(holder.btn_a,false,holder);
                        showAnswer(test4.getQuestion_remark(),false,holder);
                    }
                }
            });
            holder.btn_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearBackground(holder.btn_a,holder.btn_b,holder.btn_c,holder.btn_d);
                    if(test4.getQuestion_remark().equals("N")){
                        setBackground(holder.btn_b,true,holder);
                        Grade++;
                        setGrade(Grade);
                    }else {
                        setBackground(holder.btn_b,false,holder);
                        showAnswer(test4.getQuestion_remark(),false,holder);
                    }
                }
            });
        }else if(test4.getQuestion_type().equals("多选题")){
            Log.e("多选题--》",test4.getContent_body()+"--"+test4.getQuestion_remark());
            holder.mult_btn.setVisibility(View.VISIBLE);
            holder.mult_btn.setEnabled(false);
            //用于存放点击的是否是正确的按钮标记
            final ArrayList<ImageButton> btn=new ArrayList<>();
            btn.clear();
            btn.add(holder.btn_a);
            btn.add(holder.btn_b);
            btn.add(holder.btn_c);
            btn.add(holder.btn_d);
            content=test4.getContent_body().split("\\$#");

            holder.problem_content.setText(content[0]);
            holder.tv_a.setText(content[1]);
            holder.tv_b.setText(content[2]);
            holder.tv_c.setText(content[3]);
            holder.tv_d.setText(content[4]);
            //right1 存放标准答案，right2存放用户选项答案

            final boolean[] right1={false,false,false,false};
            final boolean[] right2={false,false,false,false};
            final boolean[] isFirst={true,true,true,true};
            //先给定用户选择选项，
            holder.btn_a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mult_btn.setEnabled(true);
                    //设置点击按钮颜色改变并判断是否为第一次点击
                    if(isFirst[0]){
                        holder.btn_a.setBackgroundResource(R.drawable.choice);
                        isFirst[0]=false;
                        //如果用户选了A就将A选项添加到right2中
                        right2[0]=true;
                    }else {
                        holder.btn_a.setBackgroundResource(R.drawable.choice0);
                        isFirst[0]=true;
                        right2[0]=false;
                    }

                }
            });
            holder.btn_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mult_btn.setEnabled(true);
                    if(isFirst[1]){
                        holder.btn_b.setBackgroundResource(R.drawable.choice);
                        isFirst[1]=false;
                        right2[1]=true;
                    }else {
                        holder.btn_b.setBackgroundResource(R.drawable.choice0);
                        isFirst[1]=true;
                        right2[1]=false;
                    }

                }
            });
            holder.btn_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mult_btn.setEnabled(true);
                    if(isFirst[2]){
                        holder.btn_c.setBackgroundResource(R.drawable.choice);
                        isFirst[2]=false;
                        right2[2]=true;
                    }else {
                        holder.btn_c.setBackgroundResource(R.drawable.choice0);
                        isFirst[2]=true;
                        right2[2]=false;
                    }

                }
            });
            holder.btn_d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mult_btn.setEnabled(true);
                    if(isFirst[3]){
                        holder.btn_d.setBackgroundResource(R.drawable.choice);
                        isFirst[3]=false;
                        right2[3]=true;
                    }else {
                        holder.btn_d.setBackgroundResource(R.drawable.choice0);
                        isFirst[3]=true;
                        right2[3]=false;
                    }

                }
            });
            //最后的确定按钮的点击事件
            holder.mult_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //确定按钮只能点击一次
                    //遍历正确与错误按钮并设置颜色和计分   right2数组用于存放标准答案，right1用于存放用户选项
                    holder.mult_btn.setEnabled(false);

                    if(test4.getQuestion_remark().contains("A")){
                        Log.i("选项","->A");
                        right1[0]=true;
                    }
                    if(test4.getQuestion_remark().contains("B")){
                        Log.i("选项","->B");
                        right1[1]=true;
                    }
                    if(test4.getQuestion_remark().contains("C")){
                        Log.i("选项","->C");
                        right1[2]=true;
                    }
                    if(test4.getQuestion_remark().contains("D")){
                        Log.i("选项","->D");
                        right1[3]=true;
                    }

                    for (int i = 0; i <right1.length; i++) {
                        Log.i("执行次数","->"+i);
                        if(right1[i]){
                            btn.get(i).setBackgroundResource(R.drawable.choice1);
                        }else {
                            btn.get(i).setBackgroundResource(R.drawable.wrong);
                        }
                    }
                    //对比两个数组的一致性，如果相同则计分一次
                    if (right1[0] == right2[0]) {
                        if (right1[1] == right2[1]) {
                            if (right1[2] == right2[2]) {
                                if (right1[3] = right2[3]) {
                                    Grade = Grade + 2;
                                    setGrade(Grade);
                                }
                            }
                        }
                    }
                    for (int i = 0; i < 4; i++) {
                        btn.get(i).setEnabled(false);
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
    private void showAnswer(String right,boolean type,Subject4ViewPagerAdapter.ViewHolder holder){
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

    //单选题和单选题的颜色改变
    private void setBackground(ImageButton btn,boolean tf,Subject4ViewPagerAdapter.ViewHolder holder){
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
