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
import com.kangzhan.student.CompayManage.AccountManage.AccountCheckingActivity;
import com.kangzhan.student.CompayManage.AccountManage.AccountCheckingInqueryActivity;
import com.kangzhan.student.CompayManage.AccountManage.MessageCheckingActivity;
import com.kangzhan.student.CompayManage.AccountManage.MessageDetailActivity;
import com.kangzhan.student.R;

/**
 * Created by kangzhan011 on 2017/7/14.
 */

public class AccountFragment extends Fragment implements View.OnClickListener{
    private View view;
    private ImageView part1,part2,titleIv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.compay_fragment_account,container,false);
            initView(view);
        }
        return view;
    }

    private void initView(View v) {
        titleIv= (ImageView) v.findViewById(R.id.compay_fragment_account_tiv);
        part1= (ImageView) v.findViewById(R.id.compay_fragment_account1);
        part2= (ImageView) v.findViewById(R.id.compay_fragment_account2);
        part1.setOnClickListener(this);
        part2.setOnClickListener(this);
        Glide.with(getActivity()).load(R.drawable.compay_account).into(titleIv);
        Glide.with(getActivity()).load(R.drawable.compay_account_part1).into(part1);
        Glide.with(getActivity()).load(R.drawable.compay_account_part2).into(part2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.compay_fragment_account1:
                Intent checking=new Intent(getContext(), AccountCheckingActivity.class);
                startActivity(checking);
                break;
            case R.id.compay_fragment_account2:
                Intent msg=new Intent(getContext(), MessageCheckingActivity.class);
                startActivity(msg);
                break;

        }
    }
}
