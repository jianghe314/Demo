package com.kangzhan.student.CompayManage.Notice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.CompayManage.Adapter.ChoiceStuAdapter;
import com.kangzhan.student.CompayManage.Adapter.CompayChoiceTeaAdapter;
import com.kangzhan.student.CompayManage.Bean.CompayChoiceStu;
import com.kangzhan.student.CompayManage.Bean.CompayChoiceTea;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
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
 * Created by kangzhan011 on 2017/8/1.
 */

public class choiceTeaFragment extends Fragment implements View.OnClickListener {
    private View view;
    private String mmsg;
    private Gson gson;
    private EditText searcherContent;
    private TextView doSearch,choiceHint;
    private ImageView choiceIv;
    private iCompayChoiceTea choiceTea;
    private LinearLayout menu1,menu2;
    private CompayChoiceTeaAdapter adapter;
    private PullRecyclerView recycler;
    private RequestQueue requestQueue;
    private ArrayList<CompayChoiceTea> mdata=new ArrayList<>();
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private int mpage=1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            recycler.stopLoadMore();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            mToast.showText(getActivity().getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 5555:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopLoadMore();
                            mToast.showText(getActivity().getApplicationContext(),"没有更多了！");
                        }
                    });
                    break;
                case 9999:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            showMessage.showMsg(getContext(),"网络连接异常，请检查网络连接");
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
            view=inflater.inflate(R.layout.item_compay_choice_object2,container,false);
            requestQueue=NoHttp.newRequestQueue();
            gson=new Gson();
            initView(view);
            initData();
        }
        return view;
    }
    private void initView(View v) {
        menu1= (LinearLayout) v.findViewById(R.id.compay_choice_obj2_menu1);
        menu1.setTag(0);
        menu2= (LinearLayout) v.findViewById(R.id.compay_choice_obj2_menu2);
        choiceIv= (ImageView) v.findViewById(R.id.compay_choice_obj2_choiceIv);
        choiceHint= (TextView) v.findViewById(R.id.compay_choice_obj2_choiceHint);
        searcherContent= (EditText) v.findViewById(R.id.compay_choice_object2_search_content);
        doSearch= (TextView) v.findViewById(R.id.compay_choice_object2_Tosearch);
        recycler= (PullRecyclerView) v.findViewById(R.id.compay_choice_obj2_recycler);
        doSearch.setOnClickListener(this);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        adapter=new CompayChoiceTeaAdapter(getContext(),R.layout.item_school_notice_choice_teacher,mdata);
    }

    private void initData() {
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.item_no_data,null));
        recycler.setLayoutManager(new XLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        //参数
                        params.add("key");
                        params.add("type");
                        //值
                        values.add(CompayInterface.CompayKey(getContext().getApplicationContext()));
                        values.add("3");
                        sendRequest("GET", CompayInterface.CompayChoiceTeacher(),1,params,values);
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {
                recycler.stopLoadMore();
                /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mpage=mpage+1;
                        params.clear();
                        values.clear();
                        //参数
                        params.add("key");
                        params.add("type");
                        params.add("page");
                        //值
                        values.add(CompayInterface.CompayKey(getContext()));
                        values.add("3");
                        values.add(mpage+"");
                        sendRequest("GET", CompayInterface.CompayChoiceTeacher(),3,params,values);
                    }
                }).start();
                */
            }
        });
        recycler.postRefreshing();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.compay_choice_object2_Tosearch:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("type");
                        params.add("searchCriteria");
                        values.add(CompayInterface.CompayKey(getContext().getApplicationContext()));
                        values.add("3");
                        values.add(searcherContent.getText().toString().trim());
                        sendRequest("GET", CompayInterface.CompayChoiceTeacher(),1,params,values);
                    }
                }).start();
                break;
            case R.id.compay_choice_obj2_menu1:
                //全选
                if(mdata.size()>0){
                    int flag= (int) menu1.getTag();
                    if(flag==0){
                        choiceIv.setImageResource(R.mipmap.choiceall1);
                        menu1.setTag(1);
                        choiceHint.setText("取消");
                        for (int i = 0; i < mdata.size(); i++) {
                            mdata.get(i).setClick(true);
                        }
                    }else {
                        choiceIv.setImageResource(R.mipmap.choiceall0);
                        menu1.setTag(0);
                        choiceHint.setText("全选");
                        for (int i = 0; i < mdata.size(); i++) {
                            mdata.get(i).setClick(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    mToast.showText(getActivity().getApplicationContext(),"没有数据");
                }
                break;
            case R.id.compay_choice_obj2_menu2:
                //添加
                if(mdata.size()>0){
                    ArrayList<CompayChoiceTea> data=new ArrayList<>();
                    for (int i = 0; i <mdata.size() ; i++) {
                        if(mdata.get(i).isClick()){
                            data.add(mdata.get(i));
                        }
                    }
                    if(data.size()>0){
                        if(choiceTea!=null){
                            choiceTea.choiceTea(data);
                            mToast.showText(getActivity().getApplicationContext(),"添加成功");
                        }
                    }else {
                        mToast.showText(getActivity().getApplicationContext(),"没有选中对象");
                    }

                }else {
                    mToast.showText(getActivity().getApplicationContext(),"没有数据");
                }
                break;

        }
    }


    private void sendRequest(String Way,String url,int what,ArrayList<String> params,ArrayList<String> values) {
        RequestMethod method=null;
        if(Way.equals("POST")){
            method=RequestMethod.POST;
        }else if(Way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,method);
        for (int i = 0; i <params.size(); i++) {
            request.add(params.get(i),values.get(i));
        }
        requestQueue.add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object = new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                mdata.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    CompayChoiceTea mange=gson.fromJson(array.getJSONObject(i).toString(),CompayChoiceTea.class);
                                    mdata.add(mange);
                                }
                                handler.sendEmptyMessage(1111);
                            }else {
                                mdata.clear();
                                handler.sendEmptyMessage(2222);
                            }
                        }else {
                            mdata.clear();
                            handler.sendEmptyMessage(2222);
                        }
                    }else if(what==3){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0) {
                                for (int i = 0; i < array.length(); i++) {
                                    CompayChoiceTea mange = gson.fromJson(array.getJSONObject(i).toString(), CompayChoiceTea.class);
                                    mdata.add(mange);
                                }
                            }
                            handler.sendEmptyMessage(1111);
                        }else {
                            handler.sendEmptyMessage(5555);
                        }
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

    public interface iCompayChoiceTea{
        void choiceTea(ArrayList<CompayChoiceTea> data);
    }
    public void choiceTeafragment(iCompayChoiceTea choiceTea){
        this.choiceTea=choiceTea;
    }
}
