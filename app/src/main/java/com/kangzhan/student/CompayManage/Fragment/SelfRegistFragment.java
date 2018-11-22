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
import com.kangzhan.student.CompayManage.SelfRegistManage.AdviseManageActivity;
import com.kangzhan.student.CompayManage.SelfRegistManage.SchoolManageActivity;
import com.kangzhan.student.CompayManage.SelfRegistManage.StudentMangeActivity;
import com.kangzhan.student.CompayManage.SelfRegistManage.TeacherManageActivity;
import com.kangzhan.student.R;

/**
 * Created by kangzhan011 on 2017/7/14.
 */

public class SelfRegistFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView part1,part2,part3,part4,titleIv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.compay_fragment_self_regist,container,false);
            initView(view);
        }
        return view;
    }

    private void initView(View v) {
        titleIv= (ImageView) v.findViewById(R.id.compay_fragment_self_regist_tiv);
        part1= (ImageView) v.findViewById(R.id.self_regist_fragment_part1);
        part2= (ImageView) v.findViewById(R.id.self_regist_fragment_part2);
        part3= (ImageView) v.findViewById(R.id.self_regist_fragment_part3);
        part4= (ImageView) v.findViewById(R.id.self_regist_fragment_part4);
        part1.setOnClickListener(this);
        part2.setOnClickListener(this);
        part3.setOnClickListener(this);
        part4.setOnClickListener(this);
        Glide.with(getActivity()).load(R.drawable.compay_self_inject_bg).into(titleIv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.self_regist_fragment_part1:
                Intent stu=new Intent(getContext(), StudentMangeActivity.class);
                startActivity(stu);
                break;
            case R.id.self_regist_fragment_part2:
                Intent teacher=new Intent(getContext(), TeacherManageActivity.class);
                startActivity(teacher);
                break;
            case R.id.self_regist_fragment_part3:
                Intent school=new Intent(getContext(), SchoolManageActivity.class);
                startActivity(school);
                break;
            case R.id.self_regist_fragment_part4:
                Intent advise=new Intent(getContext(), AdviseManageActivity.class);
                startActivity(advise);
                break;

        }
    }
}
