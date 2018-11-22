package com.kangzhan.student.School.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Edu.EduTeacherRestListActivity;
import com.kangzhan.student.School.Edu.EvaluActivity;
import com.kangzhan.student.School.Edu.ExamActivity;
import com.kangzhan.student.School.Edu.LearnHoursActivity;
import com.kangzhan.student.School.Edu.RemarkActivity;
import com.kangzhan.student.School.Edu.School_StudentDetail;
import com.kangzhan.student.School.Edu.StudentManageActivity;
import com.kangzhan.student.School.Edu.TeacherCarManageActivity;
import com.kangzhan.student.School.Edu.TeacherManageActivity;
import com.kangzhan.student.School.Edu.TeacherRestActivity;
import com.kangzhan.student.School.Edu.TestActivity;
import com.kangzhan.student.com.LazyFragment;

/**
 * Created by kangzhan011 on 2017/6/1.
 */

public class EduManagefragment extends Fragment implements View.OnClickListener{
    private View view;
    private LinearLayout l11,l12,l13,l21,l22,l23,l31,l32,l33;
    private ImageView titleIv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.school_edu_fragment,container,false);
            initView(view);
            return view;
        }else {
            return view;
        }

    }

    private void initView(View view) {
        titleIv= (ImageView) view.findViewById(R.id.school_edu_fragment);
        l11= (LinearLayout) view.findViewById(R.id.school_edu_11);
        l11.setOnClickListener(this);
        l12= (LinearLayout) view.findViewById(R.id.school_edu_12);
        l12.setOnClickListener(this);
        l13= (LinearLayout) view.findViewById(R.id.school_edu_13);
        l13.setOnClickListener(this);
        l21= (LinearLayout) view.findViewById(R.id.school_edu_21);
        l21.setOnClickListener(this);
        l22= (LinearLayout) view.findViewById(R.id.school_edu_22);
        l22.setOnClickListener(this);
        l23= (LinearLayout) view.findViewById(R.id.school_edu_23);
        l23.setOnClickListener(this);
        l31= (LinearLayout) view.findViewById(R.id.school_edu_31);
        l31.setOnClickListener(this);
        l32= (LinearLayout) view.findViewById(R.id.school_edu_32);
        l32.setOnClickListener(this);
        l33= (LinearLayout) view.findViewById(R.id.school_edu_33);
        l33.setOnClickListener(this);
        Glide.with(getActivity()).load(R.drawable.school_banner).into(titleIv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_edu_11:
                Intent stuM=new Intent(getContext(),StudentManageActivity.class);
                startActivity(stuM);
//                Intent stuM=new Intent(getContext(), School_StudentDetail.class);
//                startActivity(stuM);
                break;
            case R.id.school_edu_12:
                Intent teaM=new Intent(getContext(), TeacherManageActivity.class);
                startActivity(teaM);
                break;
            case R.id.school_edu_13:
                Intent rest=new Intent(getContext(), EduTeacherRestListActivity.class);
                startActivity(rest);
                break;
            case R.id.school_edu_21:
                Intent carM=new Intent(getContext(), TeacherCarManageActivity.class);
                startActivity(carM);
                break;
            case R.id.school_edu_22:
                Intent test=new Intent(getContext(), TestActivity.class);
                startActivity(test);
                break;
            case R.id.school_edu_23:
                Intent exam=new Intent(getContext(), ExamActivity.class);
                startActivity(exam);
                break;
            case R.id.school_edu_31:
                Intent lh=new Intent(getContext(), LearnHoursActivity.class);
                startActivity(lh);
                break;
            case R.id.school_edu_32:
                Intent evalu=new Intent(getContext(), EvaluActivity.class);
                startActivity(evalu);
                break;
            case R.id.school_edu_33:
                Intent remark=new Intent(getContext(), RemarkActivity.class);
                startActivity(remark);
                break;
            default:
                break;
        }
    }
}
