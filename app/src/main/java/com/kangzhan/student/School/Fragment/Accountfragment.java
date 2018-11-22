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
import com.kangzhan.student.School.Account.School_Account_Manage;
import com.kangzhan.student.School.Account.School_Msg_Manage;
import com.kangzhan.student.School.Account.School_qulified_set;
import com.kangzhan.student.School.Account.School_reward_record;
import com.kangzhan.student.com.LazyFragment;

/**
 * Created by kangzhan011 on 2017/6/1.
 */

public class Accountfragment extends Fragment implements View.OnClickListener{
    private View view;
    private ImageView menu1,menu2,menu3,menu4,titleIv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.school_account_fragment,container,false);
            initVeiw(view);
            return view;
        }else {
            return view;
        }

    }

    private void initVeiw(View view) {
        titleIv= (ImageView) view.findViewById(R.id.school_account_fragment_iv);
        menu1= (ImageView) view.findViewById(R.id.school_account_menu1);
        menu1.setOnClickListener(this);
        menu2= (ImageView) view.findViewById(R.id.school_account_menu2);
        menu2.setOnClickListener(this);
        menu3= (ImageView) view.findViewById(R.id.school_account_menu3);
        menu3.setOnClickListener(this);
        menu4= (ImageView) view.findViewById(R.id.school_account_menu4);
        menu4.setOnClickListener(this);
        Glide.with(getActivity()).load(R.drawable.school_account_bg).into(titleIv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_account_menu1:
                Intent m=new Intent(getContext(), School_Account_Manage.class);
                startActivity(m);
                break;
            case R.id.school_account_menu2:
                Intent mm=new Intent(getContext(), School_Msg_Manage.class);
                startActivity(mm);
                break;
            case R.id.school_account_menu3:
                Intent mm1=new Intent(getContext(), School_qulified_set.class);
                startActivity(mm1);
                break;
            case R.id.school_account_menu4:
                Intent reward=new Intent(getContext(), School_reward_record.class);
                startActivity(reward);
                break;
            default:
                break;

        }
    }
}
