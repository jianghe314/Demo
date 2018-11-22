package com.kangzhan.student.Teacher.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.Train.Teacher_TrainOrderActivity;
import com.kangzhan.student.Teacher.Train.Teacher_TrainRecordActivity;
import com.kangzhan.student.Teacher.Train.Teacher_trainGetOrderActivity;
import com.kangzhan.student.Teacher.Train.Teacher_trainTrainingActivity;
import com.kangzhan.student.com.BaseFragment;
import com.kangzhan.student.com.LazyFragment;

/**
 * Created by kangzhan011 on 2017/4/7.
 */

public class TrainFragment extends Fragment implements View.OnClickListener{
    private View view;
    private ImageButton btn1,btn2,btn3,btn4;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.teacher_train_layout,container,false);
            initView(view);
        }
        return view;
    }

    private void initView(View v) {
        btn1= (ImageButton) v.findViewById(R.id.teacher_train_qdan);
        btn1.setOnClickListener(this);
        btn2= (ImageButton) v.findViewById(R.id.teacher_train_order);
        btn2.setOnClickListener(this);
        btn3= (ImageButton) v.findViewById(R.id.teacher_train_record);
        btn3.setOnClickListener(this);
        btn4= (ImageButton) v.findViewById(R.id.teacher_train_add);
        btn4.setOnClickListener(this);
        Glide.with(getActivity()).load(R.mipmap.train_qdan).into(btn1);
        Glide.with(getActivity()).load(R.mipmap.train_order).into(btn2);
        Glide.with(getActivity()).load(R.mipmap.train_record).into(btn3);
        Glide.with(getActivity()).load(R.mipmap.teache_training).into(btn4);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_train_qdan:
                Intent qdan=new Intent(getContext(), Teacher_trainGetOrderActivity.class);
                startActivity(qdan);
                break;
            case R.id.teacher_train_order:
                Intent order=new Intent(getContext(), Teacher_TrainOrderActivity.class);
                startActivity(order);
                break;
            case R.id.teacher_train_record:
                Intent record=new Intent(getContext(), Teacher_TrainRecordActivity.class);
                startActivity(record);
                break;
            case R.id.teacher_train_add:
                Intent train=new Intent(getContext(), Teacher_trainTrainingActivity.class);
                startActivity(train);
                break;
            default:
                break;
        }
    }
}
