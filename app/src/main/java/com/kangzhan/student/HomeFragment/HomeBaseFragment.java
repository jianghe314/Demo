package com.kangzhan.student.HomeFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kangzhan.student.Debug.mLog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by kangzhan011 on 2017/11/16.
 */

public abstract class HomeBaseFragment extends Fragment  {
    private View view;
    protected boolean isShow=false;
    protected boolean isInit=false;
    private RequestQueue requestQueue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view==null){
            view=inflater.inflate(setContentView(),container,false);
            isInit=true;
            isLoadView(view);
            return view;
        }else {
            return view;
        }

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {   //此方法先与onCreateView执行
        super.setUserVisibleHint(isVisibleToUser);
        /*
        if(getUserVisibleHint()){
            isLoadView();
        }
        */
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            synchronized (RequestQueue.class){
                requestQueue= NoHttp.newRequestQueue(5);
            }
        }
        return requestQueue;
    }

    private void isLoadView(View view) {
        initView(view);
        initData();
        loadData();
        /*
        if(!isInit){
            return;
        }
        if(getUserVisibleHint()){
            initView(view);
            initData();
        }
        */

    }

    protected void loadData() {
    }

    protected void initView(View view) {
    }

    protected void initData() {
    }




    protected abstract int setContentView();

    @Override
    public void onDestroy() {
        if(requestQueue!=null){
            requestQueue.stop();
            requestQueue.cancelAll();
        }
        super.onDestroy();
    }
}
