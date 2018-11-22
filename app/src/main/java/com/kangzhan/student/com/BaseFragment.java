package com.kangzhan.student.com;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by kangzhan011 on 2017/7/21.
 */

public class BaseFragment extends Fragment {
    private BaseFragment baseFragment;

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        Fragment parentFragment=getParentFragment();
        if(parentFragment !=null &&parentFragment instanceof BaseFragment){
            ((BaseFragment) parentFragment).startActivityForResultFromChildFragment(intent,requestCode,this);
        }else {
            baseFragment=null;
            super.startActivityForResult(intent, requestCode);
        }

    }

    private void startActivityForResultFromChildFragment(Intent intent,int requestCode,BaseFragment childFragmnet){
        baseFragment=childFragmnet;
        Fragment parentFragment=getParentFragment();
        if(parentFragment!=null&&parentFragment instanceof BaseFragment){
            ((BaseFragment) parentFragment).startActivityForResultFromChildFragment(intent,requestCode,this);
        }else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(baseFragment!=null){
            baseFragment.onActivityResult(requestCode,resultCode,data);
            baseFragment=null;
        }else {
            onActivityResultCompat(requestCode,resultCode,data);
        }

    }
    public void onActivityResultCompat(int requestCode,int resultCode,Intent data){

    }
}
