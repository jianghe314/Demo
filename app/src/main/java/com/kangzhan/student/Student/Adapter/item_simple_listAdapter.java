package com.kangzhan.student.Student.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.checkObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/4/25.
 */

public class item_simple_listAdapter extends ArrayAdapter<checkObject> {
    private Context context;
    private ArrayList<checkObject> data;
    private int mresource;
    public item_simple_listAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<checkObject> data) {
        super(context, resource, data);
        this.context=context;
        mresource=resource;
        this.data=data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        checkObject check=data.get(position);
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(mresource,null);
            TextView text= (TextView) convertView.findViewById(R.id.item_list_name);
            text.setText(check.getName());
        }
        return convertView;
    }
}
