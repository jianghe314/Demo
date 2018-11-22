package com.kangzhan.student.School.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Training.School_traing_record;
import com.kangzhan.student.School.Training.School_training_order;
import com.kangzhan.student.com.LazyFragment;

/**
 * Created by kangzhan011 on 2017/6/1.
 */

public class Trainingfragment extends Fragment {
    private ImageView menu1,menu2,titleIv;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.school_training_fragment,container,false);
            initView(view);
            return view;
        }else {
            return view;
        }


    }

    private void initView(View view) {
        titleIv= (ImageView) view.findViewById(R.id.school_training_fragment);
        menu1= (ImageView) view.findViewById(R.id.school_training_menu1);
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent record=new Intent(getContext(), School_traing_record.class);
                startActivity(record);
            }
        });
        menu2= (ImageView) view.findViewById(R.id.school_training_menu2);
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order=new Intent(getContext(), School_training_order.class);
                startActivity(order);
            }
        });
        Glide.with(getActivity()).load(R.drawable.school_training_bg).into(titleIv);
    }
}
