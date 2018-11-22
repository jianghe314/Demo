package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kangzhan.student.CompayManage.Bean.AccountCoachRecord;
import com.kangzhan.student.CompayManage.Bean.AccountCoachState;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.AccountAffirmdetail_Child;
import com.kangzhan.student.School.Bean.AccountAffirmdetail_Parent;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/7/31.
 */

public class AccountSchoolDetailAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<AccountCoachState> parentData=new ArrayList<>();        //组列表项，每个组都有一个子List
    private ArrayList<ArrayList<AccountCoachRecord>> childrenData=new ArrayList<>();
    public AccountSchoolDetailAdapter(Context context, ArrayList<AccountCoachState> parent, ArrayList<ArrayList<AccountCoachRecord>> mchild) {
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
        AccountCoachState mparentData=parentData.get(groupPosition);
        View view = LayoutInflater.from(context).inflate(R.layout.item_compay_account_school_detail,parent,false);

        TextView name= (TextView) view.findViewById(R.id.item_compay_account_school_detail_name);
        TextView hour= (TextView) view.findViewById(R.id.item_compay_account_school_detail_hour);
        TextView draw= (TextView) view.findViewById(R.id.item_compay_account_school_detail_draw);
        TextView amount= (TextView) view.findViewById(R.id.item_compay_account_school_detail_amount);
        //
       name.setText(mparentData.getCoachname());
        hour.setText(mparentData.getTrain_length());
        draw.setText(mparentData.getDraw());
        amount.setText(mparentData.getAmount());
        //
        return view;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        AccountCoachRecord child=childrenData.get(groupPosition).get(childPosition);
        View  view=LayoutInflater.from(context).inflate(R.layout.item_compay_account_school_detail_child,parent,false);
        TextView time= (TextView) view.findViewById(R.id.item_compay_account_school_detail_child_time);
        TextView label= (TextView) view.findViewById(R.id.item_compay_account_school_detail_child_lable);
        TextView name= (TextView) view.findViewById(R.id.item_compay_account_school_detail_child_name);
        TextView price= (TextView) view.findViewById(R.id.item_compay_account_school_detail_child_price);
        time.setText(child.getSdate());
        label.setText(child.getLicnum());
        name.setText(child.getStuname()+" "+child.getStage());
        price.setText(child.getAmount()+"元");
        return view;
    }
    //子列表是否可选
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
