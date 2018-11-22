package com.kangzhan.student.com;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;

/**
 * Created by kangzhan011 on 2017/5/23.
 */

public abstract class LazyFragment extends Fragment {
    protected boolean isInit=false;
    protected boolean isLoad=false;
    private final String tag="LazyLoadFragment";
    private View view;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(setContentView(),container,false);
            isInit=true;
            isCanLoadData();
            return view;
        }else {
            return view;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    private void isCanLoadData() {
        if(!isInit){
            return;
        }
        if(getUserVisibleHint()){
            lazyLoad();
        }else {
            if(isLoad){
                stopLoad();
            }
        }
    }


    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            synchronized (RequestQueue.class){
                requestQueue= NoHttp.newRequestQueue(5);
            }
        }
        return requestQueue;
    }

    @Override
    public void onDestroy() {
        if(requestQueue!=null){
            requestQueue.stop();
            requestQueue.cancelAll();
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit=false;
        isLoad=false;
    }
    protected abstract int setContentView();

    protected View getContentView(){
        return view;
    }
    protected <T extends View> T findViewById(int id) {

        return (T) getContentView().findViewById(id);
    }
    protected abstract void lazyLoad();

    protected abstract void stopLoad();
}
