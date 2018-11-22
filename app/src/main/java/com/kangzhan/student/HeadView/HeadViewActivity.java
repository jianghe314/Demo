package com.kangzhan.student.HeadView;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.HeadView.HomeFragmentAdapter.mainFragmentAdapter;
import com.kangzhan.student.HomeFragment.Bean.EventMessage;
import com.kangzhan.student.HomeFragment.FindSFragment;
import com.kangzhan.student.HomeFragment.FindTFragment;
import com.kangzhan.student.HomeFragment.HomeFragment;
import com.kangzhan.student.HomeFragment.LoginFragment;
import com.kangzhan.student.HomeFragment.SchoolFragment;
import com.kangzhan.student.HomeFragment.mDialog.ErrorDialog;
import com.kangzhan.student.MainLoginActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.RecommendBean.UpdataVersion;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.AdvisetInterface.Adviset;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class HeadViewActivity extends BaseActivity implements View.OnClickListener{
    private TextView home_tv0,home_tv1,home_tv2,home_tv3;
    private ImageView home_iv0,home_iv1,home_iv2,home_iv3;
    private LinearLayout index0,index1,index2,index3;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private UpdataVersion updataVersion;
    private Gson gson=new Gson();
    private DownloadQueue downLoadQueue;
    private ProgressBar bar;
    private Dialog dialog;
    private ErrorDialog errorDialog;
    private File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_head_view);
        initView();
        initData();
        initViewPager();
        //检测版本
       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   Thread.sleep(10000);
                   checkVersion();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               //SystemClock.sleep(10000);
           }
       }).start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage msg){
        if(msg.getMsg().equals("fragmentS")){
            viewPager.setCurrentItem(1,false);
        }else if(msg.getMsg().equals("fragmentT")){
            viewPager.setCurrentItem(2,false);
        }else if(msg.getMsg().equals("check_version")){
            checkVersion();
        } else if(msg.getMsg().equals("new_version")){
            showTip();
        }else if(msg.getMsg().equals("no_new_version")){
            //没有新版本
            errorDialog.show();
            errorDialog.setTextMsg("已经是最新版本啦^_^");
        } else if(msg.getMsg().equals("download_error")){
            //下载出错
            errorDialog.show();
            errorDialog.setTextMsg("下载出错，请稍后再试");

        }else if(msg.getMsg().equals("download_start")){
            //开始下载
            starDown();
        }else if(msg.getMsg().equals("download_finish")){
            //下载完成，准备安装
            install();
        }
    }

    private void showTip() {
        AlertDialog dialog = new AlertDialog.Builder(HeadViewActivity.this)
                .setTitle("更新提示")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(updataVersion.getRemark())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //检测权限，下载更新，开启线程
                        mcheckPermission();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED);

    }

    private void starDown(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.download_layout,null);
        bar= (ProgressBar) view.findViewById(R.id.download_progressBar);
        builder.setView(view);
        builder.setTitle("下载中");
        builder.setNegativeButton("取消下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downLoadQueue.stop();
                downLoadQueue.cancelAll();
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }

    private void install(){
        File filePath=new File(getExternalFilesDir(null),"apkFile");
        File apkFile=new File(filePath,"康展学车.apk");
        mLog.e("路径2","-->"+apkFile.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            //路径1: -->/storage/emulated/0/Android/data/com.kangzhan.student/files/apkFile
            //路径2: -->/storage/emulated/0/Android/data/com.kangzhan.student/files/apkFile/康展学车.apk
            Uri apkUri = FileProvider.getUriForFile(this, "com.kangzhan.student.provider", apkFile);
            mLog.e("content","-->"+apkUri.toString());
            //content://com.kangzhan.student.provider/root_path/storage/emulated/0/Android/data/com.kangzhan.student/files/apkFile/%E5%BA%B7%E5%B1%95%E5%AD%A6%E8%BD%A6.apk
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }


    private void initView() {
        viewPager= (ViewPager) findViewById(R.id.home_main_container);
        home_tv0= (TextView) findViewById(R.id.home_tv0);
        home_tv1= (TextView) findViewById(R.id.home_tv1);
        home_tv2= (TextView) findViewById(R.id.home_tv2);
        home_tv3= (TextView) findViewById(R.id.home_tv3);
        home_iv0= (ImageView) findViewById(R.id.home_iv0);
        home_iv1= (ImageView) findViewById(R.id.home_iv1);
        home_iv2= (ImageView) findViewById(R.id.home_iv2);
        home_iv3= (ImageView) findViewById(R.id.home_iv3);
        index0= (LinearLayout) findViewById(R.id.index_0);
        index1= (LinearLayout) findViewById(R.id.index_1);
        index2= (LinearLayout) findViewById(R.id.index_2);
        index3= (LinearLayout) findViewById(R.id.index_3);
        index0.setOnClickListener(this);
        index1.setOnClickListener(this);
        index2.setOnClickListener(this);
        index3.setOnClickListener(this);
        errorDialog=new ErrorDialog(this);
    }

    private void initData() {
        fragments=new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new FindSFragment());
        fragments.add(new FindTFragment());
        fragments.add(new LoginFragment());
    }

    private void initViewPager() {
        mainFragmentAdapter mainAdapter=new mainFragmentAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(mainAdapter);
        viewPager.setCurrentItem(0,false);
        home_iv0.setImageResource(R.mipmap.home_1);
        home_tv0.setTextColor(getResources().getColor(R.color.colorPrimary));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        viewPager.setCurrentItem(0,false);
                        clearStyle();
                        home_iv0.setImageResource(R.mipmap.home_1);
                        home_tv0.setTextColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 1:
                        viewPager.setCurrentItem(1,false);
                        clearStyle();
                        home_iv1.setImageResource(R.mipmap.find_s1);
                        home_tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 2:
                        viewPager.setCurrentItem(2,false);
                        clearStyle();
                        home_iv2.setImageResource(R.mipmap.find_t1);
                        home_tv2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 3:
                        viewPager.setCurrentItem(3,false);
                        clearStyle();
                        home_iv3.setImageResource(R.mipmap.login_1);
                        home_tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.index_0:
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.index_1:
                viewPager.setCurrentItem(1,false);
                break;
            case R.id.index_2:
                viewPager.setCurrentItem(2,false);
                break;
            case R.id.index_3:
                viewPager.setCurrentItem(3,false);
                break;
        }
    }
    private void clearStyle(){
        home_tv0.setTextColor(getResources().getColor(R.color.textColor_gray));
        home_tv1.setTextColor(getResources().getColor(R.color.textColor_gray));
        home_tv2.setTextColor(getResources().getColor(R.color.textColor_gray));
        home_tv3.setTextColor(getResources().getColor(R.color.textColor_gray));
        home_iv0.setImageResource(R.mipmap.home_0);
        home_iv1.setImageResource(R.mipmap.find_s0);
        home_iv2.setImageResource(R.mipmap.find_t0);
        home_iv3.setImageResource(R.mipmap.login_0);
    }


    private void mcheckPermission() {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            int checkCallPhone= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkCallPhone!= PackageManager.PERMISSION_GRANTED){
                //没有授权,请求授权
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }else {
                //授权
                Updata();
            }
        }else {
            Updata();
        }
    }

    //权限申请回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Updata();
                }else {
                    mcheckPermission();
                    mToast.showText(getApplicationContext(),"没有权限存储文件！");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void checkVersion() {
        Request<JSONObject> request= NoHttp.createJsonObjectRequest(Adviset.checkVersion(), RequestMethod.GET);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if(what==1){
                    //解析版本升级接口数据
                    try {
                        JSONObject obj=new JSONObject(response.get().toString());
                        if(obj.getString("code").equals("200")){
                            updataVersion=gson.fromJson(obj.getString("data"),UpdataVersion.class);
                            if(getVersionCode(Integer.valueOf(updataVersion.getVersion_id()))){
                                //有新版本
                                EventBus.getDefault().post(new EventMessage("new_version"));
                            }else {
                                //没有新版本
                                EventBus.getDefault().post(new EventMessage("no_new_version"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                EventBus.getDefault().post(new EventMessage("download_error"));
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }


    //获取当前版本号
    public boolean getVersionCode(int versionCode){
        PackageManager packageManager=getPackageManager();
        PackageInfo info =null;
        try {
            info=packageManager.getPackageInfo(getPackageName(),0);
            mLog.e("InfoCode","->"+info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode>info.versionCode;
    }


    //Environment.getExternalStorageDirectory().getPath()
    public void Updata() {
        file=new File(getExternalFilesDir(null),"apkFile");
        if(file.exists()){
            file.delete();
        }
        mLog.e("路径1","-->"+file.getAbsolutePath());
        DownloadRequest request=  NoHttp.createDownloadRequest(updataVersion.getApk_url(),file.getPath(),"康展学车.apk",true,true);
        getDownLoadQueue().add(1, request, new DownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                mLog.e("DownError","->"+exception);
                EventBus.getDefault().post(new EventMessage("download_error"));
            }

            @Override
            public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
                if(allCount!=0){
                    //下载
                    EventBus.getDefault().post(new EventMessage("download_start"));
                }

            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {
                bar.setProgress(progress);
            }

            @Override
            public void onFinish(int what, String filePath) {
                //下载完成，跳转安装界面
                dialog.dismiss();
                EventBus.getDefault().post(new EventMessage("download_finish"));
            }

            @Override
            public void onCancel(int what) {

            }
        });
    }

    private DownloadQueue getDownLoadQueue(){
        if(downLoadQueue==null){
            downLoadQueue=NoHttp.newDownloadQueue();
        }
        return downLoadQueue;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if(downLoadQueue!=null){
            downLoadQueue.stop();
            downLoadQueue.cancelAll();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
