package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.bean.TeacherClass;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/5/11.
 */

public class TeacherClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<TeacherClass> data;
    private LayoutInflater inflater;
    private SetChoice setChoice;
    private final int TYPE1=1;
    private final int TYPE2=2;
    public TeacherClassAdapter(Context context, ArrayList<TeacherClass> data) {
        this.context=context;
        this.data=data;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        int type=0;
        switch (data.get(position).getType()){
            case "a":
                type=TYPE1;
                //a 未约
                break;
            case "b":
                type=TYPE1;
                //b 调休
                break;
            case "c":
                type=TYPE2;
                //c 已约
                break;
            default:
                break;
        }
        return type;
    }

    public void setChoice(SetChoice setChoice){
        this.setChoice=setChoice;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if(viewType==TYPE1){
            holder=new mViewHolder1(inflater.inflate(R.layout.item_classfragemet_layout1,parent,false));
        }else{
            holder=new mViewHolder2(inflater.inflate(R.layout.item_classfragment_layout2,parent,false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final TeacherClass mdata=data.get(position);
        //绑定数据跟(未写)点击事件
       if(getItemViewType(position)==TYPE1){
           final mViewHolder1 holder1= (mViewHolder1) holder;
           holder1.time1.setText(mdata.getStarttime()+"-"+mdata.getEndtime());
           if(mdata.getType().equals("a")){
               holder1.rest1.setTextColor(ContextCompat.getColor(context,R.color.Not_read_color));
               holder1.rest1.setText(mdata.getStatus());
           }else if(mdata.getType().equals("b")){
               holder1.rest1.setTextColor(ContextCompat.getColor(context,R.color.text_background_color_aqua));
               holder1.rest1.setText(mdata.getStatus());
           }
           if(mdata.getStage().equals("2")){
               holder1.lesson1.setText("科目二");
           }else if(mdata.getStage().equals("3")){
               holder1.lesson1.setText("科目三");
           }
           holder1.carType1.setText(mdata.getBrand());
           holder1.carLabel1.setText(mdata.getLicnum());
           holder1.price1.setText(mdata.getPrice());
           if(mdata.isclick()){
               holder1.choice1.setBackgroundResource(R.mipmap.student_test_choice1);
               holder1.carLabel1.setEnabled(true);
               holder1.price1.setEnabled(true);
               //科目选择
               holder1.arrow.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       AlertDialog.Builder builder=new AlertDialog.Builder(context);
                       builder.setItems(new String[]{"科目二", "科目三"}, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               if(which==0){
                                   holder1.lesson1.setText("科目二");
                                   mdata.setStage("2");
                               }else if(which==1){
                                   holder1.lesson1.setText("科目三");
                                   mdata.setStage("3");
                               }
                               dialog.dismiss();
                           }
                       });
                       builder.create().show();
                   }
               });
               //车牌修改
               holder1.carLabel1.addTextChangedListener(new TextWatcher() {
                   @Override
                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                   }

                   @Override
                   public void onTextChanged(CharSequence s, int start, int before, int count) {

                   }

                   @Override
                   public void afterTextChanged(Editable s) {
                       //Log.e("Text1","->"+s);
                       mdata.setLicnum(holder1.carLabel1.getText().toString().trim());
                   }
               });
               //价格修改
               holder1.price1.addTextChangedListener(new TextWatcher() {
                   @Override
                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                   }

                   @Override
                   public void onTextChanged(CharSequence s, int start, int before, int count) {

                   }

                   @Override
                   public void afterTextChanged(Editable s) {
                       //Log.e("Text2","->"+s);
                       mdata.setPrice(holder1.price1.getText().toString().trim());
                   }
               });
           }else {
               holder1.choice1.setBackgroundResource(R.mipmap.student_test_choice);
               holder1.carLabel1.setEnabled(false);
               holder1.price1.setEnabled(false);
           }
           //点击事件
       }else if(getItemViewType(position)==TYPE2){
           final mViewHolder2 holder2= (mViewHolder2) holder;
           holder2.time2.setText(mdata.getStarttime()+"-"+mdata.getEndtime());
           if(mdata.getType().equals("c")){
               holder2.rest2.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
               holder2.rest2.setText(mdata.getStatus());
           }else if(mdata.getType().equals("b")){
               holder2.rest2.setTextColor(ContextCompat.getColor(context,R.color.text_background_color_aqua));
               holder2.rest2.setText(mdata.getStatus());
           }
           if(mdata.getStage().equals("2")){
               holder2.lesson2.setText("科目二");
           }else if(mdata.getStage().equals("3")){
               holder2.lesson2.setText("科目三");
           }
           holder2.carType2.setText(mdata.getBrand());
           holder2.carLabel2.setText(mdata.getLicnum());
           holder2.price2.setText(mdata.getPrice()+"元");
           if(mdata.isclick()){
               holder2.choice2.setBackgroundResource(R.mipmap.student_test_choice1);
           }else {
               holder2.choice2.setBackgroundResource(R.mipmap.student_test_choice);
           }
       }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mdata.isclick()){
                    mdata.setIsclick(false);
                }else {
                    mdata.setIsclick(true);
                }
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class  mViewHolder1 extends RecyclerView.ViewHolder{
        ImageView choice1,arrow;
        TextView time1;
        TextView rest1;
        TextView lesson1;
        TextView carType1;
        EditText carLabel1;
        EditText price1;
        public mViewHolder1(View v1) {
            super(v1);
            choice1= (ImageView) v1.findViewById(R.id.item_classFragment_list1_choice);
            arrow= (ImageView) v1.findViewById(R.id.item_classFragment_list1_iv);
            time1= (TextView) v1.findViewById(R.id.item_classFragment_list1_time);
            rest1= (TextView) v1.findViewById(R.id.item_classFragment_list1_rest);
            lesson1= (TextView) v1.findViewById(R.id.item_classFragment_list1_lesson);
            carType1= (TextView) v1.findViewById(R.id.item_classFragment_list1_carType);
            carLabel1= (EditText) v1.findViewById(R.id.item_classFragment_list1_carLabel);
            price1= (EditText) v1.findViewById(R.id.item_classFragment_list1_price);
        }
    }
    class mViewHolder2 extends RecyclerView.ViewHolder{
        ImageView choice2;
        TextView time2;
        TextView rest2;
        TextView lesson2;
        TextView carType2;
        TextView carLabel2;
        TextView price2;
        public mViewHolder2(View v2) {
            super(v2);
            choice2= (ImageView) v2.findViewById(R.id.item_classFragment_list2_choice);
            time2= (TextView) v2.findViewById(R.id.item_classFragment_list2_time);
            rest2= (TextView) v2.findViewById(R.id.item_classFragment_list2_rest);
            lesson2= (TextView) v2.findViewById(R.id.item_classFragment_list2_lesson);
            carType2= (TextView) v2.findViewById(R.id.item_classFragment_list2_carType);
            carLabel2= (TextView) v2.findViewById(R.id.item_classFragment_list2_carLabel);
            price2= (TextView) v2.findViewById(R.id.item_classFragment_list2_price);
        }
    }

    public interface SetChoice{
        void setChoice(RecyclerView.ViewHolder holder, int postion);
    }
}
