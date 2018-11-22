package com.kangzhan.student.School.SlideMenu;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.itemChocieScopeAdapter;
import com.kangzhan.student.School.Bean.BussessScopeChoice;
import com.kangzhan.student.School.Bean.UnitInfo;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.ChoiceDateAdapter;
import com.kangzhan.student.Student.bean.ChoiceDate;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.kangzhan.student.mUI.DividerGridItemDecoration;
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

public class School_Info extends BaseActivity implements View.OnClickListener{
    private EditText linker,phone,teaSum,testSum,safeSum,carSum,roomSum,roomTheorySum,pricetiseSum,bookingDay;
    private TextView name,code,service,Scope;
    private ImageView choiceIv;
    private Switch crossSchool,crossTeacher;
    private Button save;
    private String mmsg;
    private RecyclerView recyclerView;
    private Gson gson;
    private UnitInfo info;
    private boolean isVisible=false;
    private itemChocieScopeAdapter adapter;
    private ArrayList<BussessScopeChoice> scopeData=new ArrayList<>();
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(School_Info.this,"加载中...");
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showContent();
                        }
                    });
                    break;
                case 4444:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(School_Info.this,"保存中...");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(School_Info.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //信息上传还没有搞
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school__info);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_Info_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1111);
                iniData();
            }
        }).start();
    }

    private void initView() {
        name= (TextView) findViewById(R.id.school_info_name);
        code= (TextView) findViewById(R.id.school_info_code);
        service= (TextView) findViewById(R.id.school_info_sericer);
        Scope= (TextView) findViewById(R.id.school_info_scope);
        choiceIv= (ImageView) findViewById(R.id.school_info_scope_iv);
        choiceIv.setOnClickListener(this);
        //
        linker= (EditText) findViewById(R.id.school_info_linker);
        phone= (EditText) findViewById(R.id.school_info_phone);
        teaSum= (EditText) findViewById(R.id.school_info_teaSum);
        testSum= (EditText) findViewById(R.id.school_info_testSum);
        safeSum= (EditText) findViewById(R.id.school_info_safeSum);
        carSum= (EditText) findViewById(R.id.school_info_carSum);
        roomSum= (EditText) findViewById(R.id.school_info_roomSum);
        roomTheorySum= (EditText) findViewById(R.id.school_info_roomTheorySum);
        pricetiseSum= (EditText) findViewById(R.id.school_info_pricetiseSum);
        bookingDay= (EditText) findViewById(R.id.school_info_bookingDay);
        //
        crossSchool= (Switch) findViewById(R.id.school_info_crossSchool);
        crossTeacher= (Switch) findViewById(R.id.school_info_crossTeacher);
        //
        save= (Button) findViewById(R.id.school_info_saveBtn);
        save.setOnClickListener(this);
        //

    }
    private void showContent() {
        String str="[{\"mproject\":\"A1\",\"isClick\":false},{\"mproject\":\"A2\",\"isClick\":false},{\"mproject\":\"A3\",\"isClick\":false},{\"mproject\":\"B1\",\"isClick\":false},{\"mproject\":\"B2\",\"isClick\":false},{\"mproject\":\"C1\",\"isClick\":false},{\"mproject\":\"C2\",\"isClick\":false},{\"mproject\":\"C3\",\"isClick\":false},{\"mproject\":\"C4\",\"isClick\":false},{\"mproject\":\"C5\",\"isClick\":false},{\"mproject\":\"D\",\"isClick\":false},{\"mproject\":\"E\",\"isClick\":false},{\"mproject\":\"F\",\"isClick\":false},{\"mproject\":\"M\",\"isClick\":false},{\"mproject\":\"N\",\"isClick\":false},{\"mproject\":\"P\",\"isClick\":false}]";
        try {
            JSONArray array=new JSONArray(str);
            for (int i = 0; i <array.length(); i++) {
                BussessScopeChoice scope=gson.fromJson(array.getJSONObject(i).toString(),BussessScopeChoice.class);
                scopeData.add(scope);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] mm=info.getBusiscope().split(",");
        for (int i = 0; i <mm.length; i++) {
            for (int j = 0; j <scopeData.size(); j++) {
                if(mm[i].equals(scopeData.get(j).getMproject())){
                    scopeData.get(j).setClick(true);
                }
            }
        }
        adapter=new itemChocieScopeAdapter(School_Info.this,scopeData);
        recyclerView= (RecyclerView) findViewById(R.id.item_school_info_choiceArea_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(School_Info.this,4));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(School_Info.this));
        recyclerView.setAdapter(adapter);
        //
        code.setText(info.getInscode());
        service.setText(info.getClerk());
        //
        name.setText(info.getName());
        linker.setText(info.getContact());
        phone.setText(info.getPhone());
        Scope.setText(info.getBusiscope());
        teaSum.setText(info.getCoachnumber());
        testSum.setText(info.getGrasupvnum());
        safeSum.setText(info.getSafmngnum());
        carSum.setText(info.getTracarnum());
        roomSum.setText(info.getThclassroom());
        roomTheorySum.setText(info.getClassroom());
        pricetiseSum.setText(info.getPraticefield());
        bookingDay.setText(info.getMax_days_appoint());
        if(info.getCross_inst().equals("1")){
            crossSchool.setChecked(true);
            crossSchool.setText("可以");
        }else {
            crossSchool.setChecked(false);
            crossSchool.setText("不可以");
        }
        if(info.getCross_coach().equals("1")){
            crossTeacher.setChecked(true);
            crossTeacher.setText("可以");
        }else {
            crossTeacher.setChecked(false);
            crossTeacher.setText("不可以");
        }
        crossSchool.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    crossSchool.setText("可以");
                    info.setCross_inst("1");
                }else {
                    crossSchool.setText("不可以");
                    info.setCross_inst("0");
                }
            }
        });
        crossTeacher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    crossTeacher.setText("可以");
                    info.setCross_coach("1");
                }else {
                    crossTeacher.setText("不可以");
                    info.setCross_coach("0");
                }
            }
        });

        adapter.setChoiceScopeItem(new itemChocieScopeAdapter.setChoiceScopeItem() {
            @Override
            public void ChoiceScopeItem(int position) {
                StringBuilder builder=new StringBuilder();
                for (int i = 0; i <scopeData.size(); i++) {
                    if(scopeData.get(i).isClick()){
                        builder.append(scopeData.get(i).getMproject());
                        builder.append(",");
                    }
                }
                if(builder.toString().length()>0){
                    Scope.setText(builder.toString().substring(0,builder.toString().length()-1));
                }else {
                    Scope.setText("");
                }
                info.setBusiscope(builder.toString());
            }
        });


    }

    private void iniData() {
        params.clear();
        values.clear();
        params.add("key");
        values.add(school.schoolKey(getApplicationContext()));
        sendRequest("POST",1, school.UnitInfo(),params,values);
    }

    private void sendRequest(String way, int what, String url, ArrayList<String> params,ArrayList<String> values) {
        RequestMethod method=null;
        if(way.equals("POST")){
            method=RequestMethod.POST;
        }else if(way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,method);
        for (int i = 0; i < params.size(); i++) {
            request.add(params.get(i),values.get(i));
        }
        getRequestQueue().add(what, request, new OnResponseListener<JSONObject>() {
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
                            info=gson.fromJson(array.getJSONObject(0).toString(),UnitInfo.class);
                            handler.sendEmptyMessage(3333);
                        }else {
                            handler.sendEmptyMessage(2222);
                        }
                    }else if(what==2){
                        handler.sendEmptyMessage(2222);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_info_saveBtn:
                //上传修改信息
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(4444);
                        params.clear();
                        values.clear();
                        //
                        params.add("key");
                        params.add("name");
                        params.add("shortname");
                        params.add("licnum");
                        params.add("licetime");
                        params.add("business");
                        params.add("creditcode");
                        params.add("location");
                        params.add("address");
                        params.add("postcode");
                        params.add("legal");
                        params.add("contact");
                        params.add("phone");
                        params.add("busiscope");
                        params.add("busistatus");
                        params.add("level");
                        params.add("coachnumber");
                        params.add("grasupvnum");
                        params.add("safmngnum");
                        params.add("tracarnum");
                        params.add("classroom");
                        params.add("thclassroom");
                        params.add("praticefield");
                        params.add("cross_inst");
                        params.add("cross_coach");
                        params.add("max_days_appoint");
                        //
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(info.getName());
                        values.add(info.getShortname());
                        values.add(info.getLicnum());
                        values.add(info.getLicetime());
                        values.add(info.getBusiness());
                        values.add(info.getCreditcode());
                        values.add(info.getProvince_id()+info.getCity_id()+info.getCounty_id());
                        values.add(info.getAddress());
                        values.add(info.getPostcode());
                        values.add(info.getLegal());
                        values.add(linker.getText().toString().trim());
                        values.add(phone.getText().toString().trim());
                        values.add(info.getBusiscope());
                        values.add(info.getBusistatus());
                        values.add(info.getLevel());
                        values.add(teaSum.getText().toString().trim());
                        values.add(testSum.getText().toString().trim());
                        values.add(safeSum.getText().toString().trim());
                        values.add(carSum.getText().toString().trim());
                        values.add(roomSum.getText().toString().trim());
                        values.add(roomTheorySum.getText().toString().trim());
                        values.add(pricetiseSum.getText().toString().trim());
                        values.add(info.getCross_inst());
                        values.add(info.getCross_coach());
                        values.add(bookingDay.getText().toString().trim());
                        sendRequest("POST",2,school.ModifUnitInfo(),params,values);
                    }
                }).start();
                break;
            case R.id.school_info_scope_iv:
                if(isVisible){
                    recyclerView.setVisibility(View.GONE);
                    isVisible=false;
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                    isVisible=true;
                }
                break;
            default:
                break;
        }
    }

}
