package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.RemarkRecord;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/27.
 */

public class RemarkRecordAdapter extends BaseRecyclerAdapter<RemarkRecord> {
    private Context context;
    private ArrayList<RemarkRecord> data;
    public RemarkRecordAdapter(Context context, int layoutResId, ArrayList<RemarkRecord> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, RemarkRecord item) {
        TextView teahcer= (TextView) holder.getView().findViewById(R.id.compay_teaching_reamrk_teacher);
        TextView student= (TextView) holder.getView().findViewById(R.id.compay_teaching_reamrk_student);
        TextView time= (TextView) holder.getView().findViewById(R.id.compay_teaching_reamrk_time);
        TextView content= (TextView) holder.getView().findViewById(R.id.compay_teaching_reamrk_content);
        teahcer.setText("教练姓名："+item.getCoaname());
        student.setText("学员姓名："+item.getStuname());
        time.setText("评价时间："+item.getCreate_time());
        content.setText(item.getContent());
    }
}
