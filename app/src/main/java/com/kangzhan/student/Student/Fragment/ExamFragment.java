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
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Exam.Subject1Activity;
import com.kangzhan.student.Student.Exam.Subject4Activity;
import com.kangzhan.student.R;
import com.kangzhan.student.com.BaseFragment;
import com.kangzhan.student.com.LazyFragment;

/**
 * Created by zhengxuan on 2017/3/27.
 */

public class ExamFragment extends Fragment implements View.OnClickListener{
    private View view;
    private ImageView content1,content2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.activity_exam,container,false);
            initView(view);
        }
        return view;
    }

    private void initView(View v) {
        content1= (ImageView) v.findViewById(R.id.exam_content1);
        content1.setOnClickListener(this);
        content2= (ImageView) v.findViewById(R.id.exam_content2);
        content2.setOnClickListener(this);
        Glide.with(getActivity()).load(R.drawable.student_ke1).into(content1);
        Glide.with(getActivity()).load(R.drawable.student_ke4).into(content2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //科目一
            case R.id.exam_content1:
                Intent content1=new Intent(getContext(), Subject1Activity.class);
                startActivity(content1);
                break;
            //科目四
            case R.id.exam_content2:
                Intent content2=new Intent(getContext(), Subject4Activity.class);
                startActivity(content2);
                break;
        }
    }
}
