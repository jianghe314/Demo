package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.EvaluRecord;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/27.
 */

public class EvaluRecordAdapter extends BaseRecyclerAdapter<EvaluRecord> {
    private ArrayList<EvaluRecord> data;
    private Context context;
    public EvaluRecordAdapter(Context context, int layoutResId, ArrayList<EvaluRecord> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;

    }

    @Override
    protected void convert(BaseViewHolder holder, EvaluRecord item) {
        TextView teacher= (TextView) holder.getView().findViewById(R.id.item_compay_evalu_teacher);
        TextView student= (TextView) holder.getView().findViewById(R.id.item_compay_evalu_student);
        TextView score= (TextView) holder.getView().findViewById(R.id.item_compay_evalu_score);
        //
        teacher.setText(item.getCoaname());
        student.setText(item.getStuname());
        score.setText(item.getOverall());
    }
}
