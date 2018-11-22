package com.kangzhan.student.CompayManage.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kangzhan.student.CompayManage.InfoManage.CompaySchoolManage;
import com.kangzhan.student.CompayManage.InfoManage.CompayStuManageActivity;
import com.kangzhan.student.CompayManage.InfoManage.CompayTeacManage;
import com.kangzhan.student.CompayManage.InfoManage.TeachCarManage;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Edu.TeacherCarManageActivity;

/**
 * Created by kangzhan011 on 2017/7/14.
 */

public class InfoFragment extends Fragment implements View.OnClickListener{
    private View view;
    private ImageView part1,part2,part3,part4,titleIv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.compay_fragment_info,container,false);
            initView(view);
        }
        return view;
    }

    private void initView(View v) {
        titleIv= (ImageView) v.findViewById(R.id.compay_fragment_info_tiv);
        part1= (ImageView) v.findViewById(R.id.compay_fragment_part1);
        part2= (ImageView) v.findViewById(R.id.compay_fragment_part2);
        part3= (ImageView) v.findViewById(R.id.compay_fragment_part3);
        part4= (ImageView) v.findViewById(R.id.compay_fragment_part4);
        part1.setOnClickListener(this);
        part2.setOnClickListener(this);
        part3.setOnClickListener(this);
        part4.setOnClickListener(this);
        Glide.with(getActivity()).load(R.drawable.compay_info).into(titleIv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.compay_fragment_part1:
                Intent stu=new Intent(getContext(), CompayStuManageActivity.class);
                startActivity(stu);
                break;
            case R.id.compay_fragment_part2:
                Intent tea=new Intent(getContext(), CompayTeacManage.class);
                startActivity(tea);
                break;
            case R.id.compay_fragment_part3:
                Intent school=new Intent(getContext(), CompaySchoolManage.class);
                startActivity(school);
                break;
            case R.id.compay_fragment_part4:
                Intent car=new Intent(getContext(), TeachCarManage.class);
                startActivity(car);
                break;
        }
    }
}
