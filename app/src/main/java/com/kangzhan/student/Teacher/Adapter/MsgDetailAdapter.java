package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.bean.MsgDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by kangzhan011 on 2017/4/11.
 */

public class MsgDetailAdapter extends BaseRecyclerAdapter<MsgDetail> {
    private Context context;
    private ArrayList<MsgDetail> data;
    public MsgDetailAdapter(Context context, int layoutResId, ArrayList<MsgDetail> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, MsgDetail item) {
        TextView time= (TextView) holder.getView().findViewById(R.id.msg_detail_date);
        TextView phone= (TextView) holder.getView().findViewById(R.id.msg_detail_Phone);
        //
        time.setText(item.getTime());
        phone.setText(item.getPhone());
    }
}
