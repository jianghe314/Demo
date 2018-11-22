package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.AccountAffirmdetail_Child;
import com.kangzhan.student.School.Bean.AccountAffirmdetail_Parent;
import com.kangzhan.student.Teacher.bean.ExpandChild;
import com.kangzhan.student.Teacher.bean.ExpandReward;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/7/6.
 */

public class AccountAffirmDetailAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<AccountAffirmdetail_Parent> parentData=new ArrayList<>();        //组列表项，每个组都有一个子List
    private ArrayList<ArrayList<AccountAffirmdetail_Child>> childrenData=new ArrayList<>();
    public AccountAffirmDetailAdapter(Context context,ArrayList<AccountAffirmdetail_Parent> parent,ArrayList<ArrayList<AccountAffirmdetail_Child>> mchild) {
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
        AccountAffirmdetail_Parent mparentData=parentData.get(groupPosition);
        View view = LayoutInflater.from(context).inflate(R.layout.item_school_account_detail_parent,parent,false);

        TextView name= (TextView) view.findViewById(R.id.item_school_account_detail_name);
        //
        name.setText(mparentData.getContent());
        //
        return view;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        AccountAffirmdetail_Child child=childrenData.get(groupPosition).get(childPosition);
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
        address.setText(child.getStage());
        return view;
    }
    //子列表是否可选
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
