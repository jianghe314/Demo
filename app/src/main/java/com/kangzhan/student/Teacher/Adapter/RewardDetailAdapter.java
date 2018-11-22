package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.bean.ExpandChild;
import com.kangzhan.student.Teacher.bean.ExpandReward;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/4/10.
 */

public class RewardDetailAdapter extends BaseExpandableListAdapter{
    private Context context;
    private ArrayList<ExpandReward> parentData;
    private ArrayList<ArrayList<ExpandChild>> childrenData;
    public RewardDetailAdapter(Context context,ArrayList<ExpandReward> parent,ArrayList<ArrayList<ExpandChild>> children) {
        this.context=context;
        this.parentData=parent;
        this.childrenData=children;
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
        ExpandReward mparentData=parentData.get(groupPosition);
        View view =LayoutInflater.from(context).inflate(R.layout.item_reward_detail_expd,parent,false);

        TextView name= (TextView) view.findViewById(R.id.ex_name);
        TextView time= (TextView) view.findViewById(R.id.ex_date);
        TextView lesson= (TextView) view.findViewById(R.id.ex_lesson);
        TextView reward= (TextView) view.findViewById(R.id.ex_reward);
        TextView hour= (TextView) view.findViewById(R.id.ex_hour);
        //
        name.setText(mparentData.getStudent_name());
        time.setText(mparentData.getExam_date());
        lesson.setText(mparentData.getExam_name());
        reward.setText(mparentData.getAmount());
        //
        hour.setText(mparentData.getLength());
        return view;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ExpandChild child=childrenData.get(groupPosition).get(childPosition);
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
