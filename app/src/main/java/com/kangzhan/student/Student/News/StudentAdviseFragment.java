package com.kangzhan.student.Student.News;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.AdviseAdapter;
import com.kangzhan.student.Student.bean.Advise;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.com.BaseFragment;
import com.kangzhan.student.com.LazyFragment;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/4/18.
 */

public class StudentAdviseFragment extends Fragment {
    private View view;
    private TextView sum_Num;
    private RequestQueue requestQueue;
    private Advise advise;
    private Gson gson;
    private String mmsg;
    private ArrayList<Advise> mdata=new ArrayList<>();
    private AdviseAdapter adapter;
    private PullRecyclerView recycler;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            adapter.notifyDataSetChanged();
                            sum_Num.setText(""+mdata.size());
                        }
                    });
                    break;
                case 1111:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            mToast.showText(getActivity().getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 9999:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                            builder.setMessage("网络连接异常，请检测网络连接");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    });
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.tabhost_advise,container,false);
            requestQueue=NoHttp.newRequestQueue();
            gson=new Gson();
            initView(view);
        }
        return view;
    }

    private void initView(View v) {
        sum_Num= (TextView) v.findViewById(R.id.notice_Num);
        adapter=new AdviseAdapter(getContext(),R.layout.item_list_advise_adapter,mdata);
        recycler= (PullRecyclerView) v.findViewById(R.id.advise_list);
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.item_no_data,null));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.setLayoutManager(new XLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest();
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {
                recycler.stopLoadMore();
            }
        });
        recycler.postRefreshing();
    }


    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentMsgAdvise(), RequestMethod.GET);
        request.add("key",student.studentKey(getContext()));
        requestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    mLog.i("response-->",""+response.get().toString());
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    mmsg=object.getString("msg");
                    if(code.equals("200")){
                        JSONArray array=new JSONArray(object.getString("data"));
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length() ; i++) {
                                JSONObject obj=array.getJSONObject(i);
                                advise=gson.fromJson(obj.toString(),Advise.class);
                                mdata.add(advise);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        handler.sendEmptyMessage(0000);
                    }else {
                        handler.sendEmptyMessage(1111);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                handler.sendEmptyMessage(9999);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }


    @Override
    public void onDestroy() {
        requestQueue.cancelAll();
        super.onDestroy();
    }
}
