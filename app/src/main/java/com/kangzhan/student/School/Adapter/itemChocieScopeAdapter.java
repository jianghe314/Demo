package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.BussessScopeChoice;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/6/28.
 */

public class itemChocieScopeAdapter extends RecyclerView.Adapter<itemChocieScopeAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<BussessScopeChoice> data;
    private setChoiceScopeItem choiceItem;
    public void setChoiceScopeItem(setChoiceScopeItem choiceItem){
        this.choiceItem=choiceItem;
    }

    public itemChocieScopeAdapter(Context context, ArrayList<BussessScopeChoice> data) {
        this.context=context;
        this.data=data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder=new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_school_scope_choice,parent,false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txt.setText(data.get(position).getMproject());
        //已选中效果
        if(data.get(position).isClick()){
            holder.txt.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.bg.setBackgroundColor(ContextCompat.getColor(context,R.color.background_color1));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.get(position).isClick()){
                    holder.txt.setTextColor(ContextCompat.getColor(context,R.color.textColor_gray));
                    holder.bg.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_whit));
                    data.get(position).setClick(false);
                    if(choiceItem!=null){
                        choiceItem.ChoiceScopeItem(position);
                    }
                }else {
                    holder.txt.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                    holder.bg.setBackgroundColor(ContextCompat.getColor(context,R.color.background_color1));
                    data.get(position).setClick(true);
                    if(choiceItem!=null){
                        choiceItem.ChoiceScopeItem(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        FrameLayout bg;
        public MyViewHolder(View itemView) {
            super(itemView);
            txt= (TextView) itemView.findViewById(R.id.item_school_scope_choice);
            bg= (FrameLayout) itemView.findViewById(R.id.item_school_scope_bg);
        }
    }
    public interface setChoiceScopeItem{
        void ChoiceScopeItem(int position);
    }

}
