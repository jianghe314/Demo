package com.kangzhan.student;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.Advisetment.RecomendSchool;
import com.kangzhan.student.Advisetment.RecommendInstActivity;
import com.kangzhan.student.Advisetment.RecommendNewsActivity;
import com.kangzhan.student.Advisetment.RecommendTeacher;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.RecommendBean.UpdataVersion;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.AdvisetInterface.Adviset;
import com.kangzhan.student.mUI.AutoVerticalScrollTextView;
import com.kangzhan.student.mUI.mBanner;
import com.kangzhan.student.utils.framework.AppConfig;
import com.yanzhenjie.nohttp.ByteArrayBinary;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.ByteArrayRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainLoginActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout student,teacher,Recommendschool,recommendTeacher,news,teach,school,Compay;
    private mBanner bannerView;
    private List<Integer> imageUrl=new ArrayList<>();
    private int number=0;
    public static boolean isForeground = false;
    private boolean isRunning=true;
    private AutoVerticalScrollTextView autoText;
    private DownloadQueue downLoadQueue;
    private ProgressBar bar;
    private Dialog dialog;
    private File file;
    private Gson gson;
    private UpdataVersion updataVersion;
    private String[] text={"全国机动车驾培行业培训服务模式改革覆盖74.5%","长沙机动车保有量超200万辆","外地人凭居住证就可以参加驾考"};
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            autoText.next();
                            number++;
                            autoText.setText(text[number%text.length]);
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog dialog = new AlertDialog.Builder(MainLoginActivity.this)
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
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //下载界面
                            AlertDialog.Builder builder=new AlertDialog.Builder(MainLoginActivity.this);
                            View view= LayoutInflater.from(MainLoginActivity.this).inflate(R.layout.download_layout,null);
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
                    });
                    break;
                case 6666:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            File filePath=new File(getExternalFilesDir(null),"apkFile");
                            File apkFile=new File(filePath,"康展学车.apk");
                            mLog.e("路径2","-->"+apkFile.getAbsolutePath());
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
                                //路径1: -->/storage/emulated/0/Android/data/com.kangzhan.student/files/apkFile
                                //路径2: -->/storage/emulated/0/Android/data/com.kangzhan.student/files/apkFile/康展学车.apk
                                Uri apkUri = FileProvider.getUriForFile(MainLoginActivity.this, "com.kangzhan.student.provider", apkFile);
                                mLog.e("content","-->"+apkUri.toString());
                                //content://com.kangzhan.student.provider/root_path/storage/emulated/0/Android/data/com.kangzhan.student/files/apkFile/%E5%BA%B7%E5%B1%95%E5%AD%A6%E8%BD%A6.apk
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                            } else {
                                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                            }
                            startActivity(intent);
                        }

                    });
                    break;
                case 8888:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mToast.showText(MainLoginActivity.this,"版本升级检测接口出错了^$^");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMessage.showMsg(MainLoginActivity.this,"下载服务器开小差了");
                        }
                    });
                    break;
            }
        }
    };

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

    /*
    先在这个界面放两个按钮
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.mainloginactivity_layout);
        //initView();
    }

    private void initView() {
        bannerView= (mBanner) findViewById(R.id.main_login_banner);
        bannerView.startSmoothAuto();

        bannerView.setOnItemClickListener(new mBanner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent detail=new Intent();
                detail.setAction("android.intent.action.VIEW");
                Uri uri=Uri.parse("http://kzxueche.com/");
                detail.setData(uri);
                startActivity(detail);
            }
        });

        Recommendschool= (RelativeLayout) findViewById(R.id.main_login_11);
        Recommendschool.setOnClickListener(this);
        teach= (RelativeLayout) findViewById(R.id.main_login_13);
        teach.setOnClickListener(this);
        recommendTeacher= (RelativeLayout) findViewById(R.id.main_login_12);
        recommendTeacher.setOnClickListener(this);
        student= (RelativeLayout) findViewById(R.id.main_login_21);
        student.setOnClickListener(this);
        teacher= (RelativeLayout) findViewById(R.id.main_login_22);
        teacher.setOnClickListener(this);
        school= (RelativeLayout) findViewById(R.id.main_login_23);
        school.setOnClickListener(this);
        Compay= (RelativeLayout) findViewById(R.id.main_login_33);
        Compay.setOnClickListener(this);
        news= (RelativeLayout) findViewById(R.id.main_login_32);
        news.setOnClickListener(this);
        //autoText= (AutoVerticalScrollTextView) findViewById(R.id.main_login_atuoText);
        autoText.setText(text[0]);
        gson=new Gson();
        new Thread(){
            @Override
            public void run() {
                while (isRunning){
                    SystemClock.sleep(3000);
                    handler.sendEmptyMessage(1111);
                }
            }
        }.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //检测版本更新
                checkVersion();
            }
        }).start();
    }

    private void checkVersion() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(Adviset.checkVersion(),RequestMethod.GET);
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
                                handler.sendEmptyMessage(2222);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                handler.sendEmptyMessage(8888);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_login_11:
                //驾校推介
                Intent rschool=new Intent(this, RecomendSchool.class);
                rschool.putExtra("id","34");
                startActivity(rschool);
                break;
            case R.id.main_login_12:
                //教练推介
                Intent reTeacher=new Intent(this, RecommendTeacher.class);
                reTeacher.putExtra("id","26");
                startActivity(reTeacher);
                break;
            case R.id.main_login_13:
                Intent inst=new Intent(this, RecommendInstActivity.class);
                startActivity(inst);
                break;
            case R.id.main_login_21:
                Intent student=new Intent(this,Student_Login.class);
                startActivity(student);
                finish();
                break;
            case R.id.main_login_22:
                Intent teacher=new Intent(this,Teacher_Login.class);
                startActivity(teacher);
                finish();
                break;
            case R.id.main_login_23:
                //驾校登录
                Intent school=new Intent(this,School_Login.class);
                startActivity(school);
                finish();
                break;
            case R.id.main_login_31:
                //运管登录

                break;
            case R.id.main_login_32:
                //康康资讯
                Intent mnews=new Intent(this,RecommendNewsActivity.class);
                startActivity(mnews);
                break;
            case R.id.main_login_33:
                //员工登录
                Intent compay=new Intent(this,CompayManage_Login.class);
                startActivity(compay);
                finish();
                break;
        }
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
    private void Updata() {
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
                handler.sendEmptyMessage(9999);
           }

           @Override
           public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
               if(allCount!=0){
                   handler.sendEmptyMessage(3333);
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
                handler.sendEmptyMessage(6666);
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
    protected void onResume() {
        isForeground = true;
        bannerView.onResume();
        super.onResume();

    }


    @Override
    protected void onPause() {
        isForeground = false;
        bannerView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        isRunning=false;
        if(downLoadQueue!=null){
            downLoadQueue.stop();
            downLoadQueue.cancelAll();
        }
        super.onDestroy();
    }
}
