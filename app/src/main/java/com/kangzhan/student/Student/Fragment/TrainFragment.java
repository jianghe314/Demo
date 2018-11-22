package com.kangzhan.student.Student.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Train.TrainMapActivity;
import com.kangzhan.student.Student.Train.Train_BookingActivity;
import com.kangzhan.student.Student.Train.Train_recordActivity;
import com.kangzhan.student.com.BaseFragment;
import com.kangzhan.student.com.LazyFragment;

import static com.kangzhan.student.R.id.train_tv1;
import static com.kangzhan.student.R.id.train_tv2;
import static com.kangzhan.student.R.id.train_tv3;
import static com.kangzhan.student.R.id.train_tv4;

/**
 * Created by zhengxuan on 2017/3/27.
 */

public class TrainFragment extends Fragment implements View.OnClickListener{
    private View view ;
    private ImageView tv1,tv2,tv3,tv4;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.activity_train,container,false);
            initView(view);
        }
        return view;
    }

    private void initView(View v) {
        tv1= (ImageView) v.findViewById(train_tv1);      //陪练申请
        tv1.setOnClickListener(this);
        tv2= (ImageView) v.findViewById(train_tv2);      //陪练订单
        tv2.setOnClickListener(this);
        tv3= (ImageView) v.findViewById(train_tv3);      //陪练记录
        tv3.setOnClickListener(this);
        tv4= (ImageView) v.findViewById(train_tv4);      //添加
        tv4.setOnClickListener(this);
        Glide.with(getActivity()).load(R.drawable.student_train_apply).into(tv1);
        Glide.with(getActivity()).load(R.drawable.student_train_order).into(tv2);
        Glide.with(getActivity()).load(R.drawable.student_train_record).into(tv3);
        Glide.with(getActivity()).load(R.drawable.student_train_add).into(tv4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case train_tv1:
                Intent apply=new Intent(getContext(), TrainMapActivity.class);
                startActivity(apply);
                break;
            case train_tv2:
                Intent booking=new Intent(getContext(), Train_BookingActivity.class);
                startActivity(booking);
                break;
            case train_tv3:
                Intent record=new Intent(getContext(), Train_recordActivity.class);
                startActivity(record);
                break;
            case train_tv4:
                Toast.makeText(getActivity().getApplicationContext(),"该功能尚未开发，敬请期待",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
