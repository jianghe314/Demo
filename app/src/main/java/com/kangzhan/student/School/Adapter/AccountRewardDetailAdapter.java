package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.Account_reward_detail_Title;
import com.kangzhan.student.School.Bean.Account_reward_detail_content;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/7/7.
 */

public class AccountRewardDetailAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Account_reward_detail_Title> parentData=new ArrayList<>();        //组列表项，每个组都有一个子List
    private ArrayList<ArrayList<Account_reward_detail_content>> childrenData=new ArrayList<>();
    public AccountRewardDetailAdapter(Context context,ArrayList<Account_reward_detail_Title> parent,ArrayList<ArrayList<Account_reward_detail_content>> mchild) {
        this.context=context;
        this.parentData=parent;
        this.childrenData=mchild;
    }

    @Override
    public int getGroupCount() {
        return parentData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(childrenData.get(groupPosition).size()>0){
            return childrenData.get(groupPosition).size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childrenData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Account_reward_detail_Title mparentData=parentData.get(groupPosition);
        View view = LayoutInflater.from(context).inflate(R.layout.item_school_account_reward_detail,parent,false);
        TextView time= (TextView) view.findViewById(R.id.item_school_account_reward_detail_time);
        TextView name= (TextView) view.findViewById(R.id.item_school_account_reward_detail_name);
        TextView subject= (TextView) view.findViewById(R.id.item_school_account_reward_detail_subject);
        TextView price= (TextView) view.findViewById(R.id.item_school_account_reward_detail_price);
        TextView hours= (TextView) view.findViewById(R.id.item_school_account_reward_detail_hours);
        //
        time.setText(mparentData.getExam_date());
        name.setText(mparentData.getStudent_name());
        subject.setText(mparentData.getStudent_name());
        price.setText(mparentData.getAmount());
        hours.setText(mparentData.getLength());
        //
        return view;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Account_reward_detail_content child=childrenData.get(groupPosition).get(childPosition);
        View  view=LayoutInflater.from(context).inflate(R.layout.item_reward_child,parent,false);
        TextView type= (TextView) view.findViewById(R.id.child_tv1);
        if(childPosition==0){
            type.setVisibility(View.VISIBLE);
        }else {
            type.setVisibility(View.INVISIBLE);
        }
        TextView time= (TextView) view.findViewById(R.id.child_date);
        TextView label= (TextView) view.findViewById(R.id.child_car_label);
        TextView address= (TextView) view.findViewById(R.id.child_address);

        //
        time.setText(child.getTime());
        label.setText(child.getLicnum());
        address.setText(child.getStage_name());
        return view;
    }
    //子列表是否可选
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
