package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.AccountMsgDetail;
import com.kangzhan.student.School.Bean.AccountMsgDetailContent;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/7/6.
 */

public class OneLineSimpleAdapter extends BaseAdapter {
    private ArrayList<AccountMsgDetailContent> data;
    private Context context;
    private int ResouceId;
    public OneLineSimpleAdapter(Context context, ArrayList<AccountMsgDetailContent> data,int resourId) {
        this.data=data;
        this.context=context;
        ResouceId=resourId;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AccountMsgDetailContent item=data.get(position);
        View view= LayoutInflater.from(context).inflate(ResouceId,null);
        TextView txt= (TextView) view.findViewById(R.id.item_simple_txt);
        txt.setText(item.getContent());
        return view;
    }
}
