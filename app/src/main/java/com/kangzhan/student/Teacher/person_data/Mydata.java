package com.kangzhan.student.Teacher.person_data;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.kangzhan.student.BuildConfig;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.ForgotPasswordActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.person_data_activity.PersondataActivity;
import com.kangzhan.student.Teacher.bean.TeacherInfo;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.kangzhan.student.utils.FileUtils;
import com.kangzhan.student.utils.ImageCompress;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class Mydata extends BaseActivity implements View.OnClickListener{

    private EditText phone_Num,address,qq,wechat,email,id,carInfo,price;
    private CircleImageView header;
    private TextView name,sex,train,evolu,school;
    private Button save;
    private Gson gson;
    private String mmsg,msex;
    private TeacherInfo info;
    //
    //更换头像
    private File mTmpFile;
    private File mCropImageFile;
    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY  = 101;
    private static final int REQUEST_CROP  = 102;
    private BottomSheetDialog dialog;
    private String[] strSex={"男","女"};
    //
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Mydata.this,"加载中...");
                        }
                    });
                    break;
                case 0011:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Mydata.this,"发送中...");
                        }
                    });
                    break;
                case 1122:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            name.setText(info.getName());
                            sex.setText(info.getSex());
                            phone_Num.setText(info.getMobile());
                            address.setText(info.getAddress());
                            qq.setText(info.getQqnum());
                            wechat.setText(info.getWechatnum());
                            email.setText(info.getEmail());
                            id.setText(info.getOccupationno());
                            carInfo.setText(info.getBrand()+"-"+info.getLicnum());
                            train.setText(info.getSubject_name());
                            price.setText(info.getSubject_price()+"元/时");
                            evolu.setText(info.getScore());
                            school.setText(info.getInst_name());
                            Glide.with(Mydata.this).load(teacher.teacherPhoto(Mydata.this)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).error(R.drawable.header).crossFade().into(header);
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Mydata.this,"上传中...");
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            Toast.makeText(getApplicationContext(),mmsg,Toast.LENGTH_SHORT).show();

                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(Mydata.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydata);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_mydata_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        header= (CircleImageView) findViewById(R.id.teacher_mydata_header);
        header.setOnClickListener(this);
        name= (TextView) findViewById(R.id.teacher_mydata_name);
        sex= (TextView) findViewById(R.id.teacher_mydata_sex);
        phone_Num= (EditText) findViewById(R.id.teacher_mydata_Phone);
        address= (EditText) findViewById(R.id.teacher_mydata_address);
        qq= (EditText) findViewById(R.id.teacher_mydata_qq);
        wechat= (EditText) findViewById(R.id.teacher_mydata_wechat);
        email= (EditText) findViewById(R.id.teacher_mydata_email);
        id= (EditText) findViewById(R.id.teacher_mydata_ID);
        carInfo= (EditText) findViewById(R.id.teacher_mydata_carInfo);
        train= (TextView) findViewById(R.id.teacher_mydata_train);
        price= (EditText) findViewById(R.id.teacher_mydata_price);
        evolu= (TextView) findViewById(R.id.teacher_mydata_eval);
        school= (TextView) findViewById(R.id.teacher_mydata_school);
        save= (Button) findViewById(R.id.teacher_mydata_save);
        save.setOnClickListener(this);
        sex.setOnClickListener(this);
        Message msg=new Message();
        msg.what=0000;
        handler.sendMessage(msg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest();
            }
        }).start();
    }

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherMydata(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    if(code.equals("200")){
                        String data=object.getString("data");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            for (int i = 0; i <array.length(); i++) {
                                JSONObject obj=array.getJSONObject(i);
                                info=gson.fromJson(obj.toString(),TeacherInfo.class);
                            }
                            handler.sendEmptyMessage(1111);
                        }
                    }else {
                        Message msg=new Message();
                        msg.what=9999;
                        handler.sendMessage(msg);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.teacher_mydata_edit:
                chanceEdit();
                break;
        }
        return true;
    }

    private void chanceEdit() {
        sex.setEnabled(true);
        phone_Num.setEnabled(true);
        address.setEnabled(true);
        qq.setEnabled(true);
        wechat.setEnabled(true);
        email.setEnabled(true);
        id.setEnabled(true);
        carInfo.setEnabled(true);
        price.setEnabled(true);
        save.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_mydata_edit,menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_mydata_save:
                if(isRight()){
                    final Dialog dialog=new Dialog(Mydata.this);
                    View view=LayoutInflater.from(Mydata.this).inflate(R.layout.item_change_mydata,null);
                    final EditText phoneNum= (EditText) view.findViewById(R.id.item_change_mydata_phone);
                    final EditText code= (EditText) view.findViewById(R.id.item_change_mydata_getCode);
                    Button send= (Button) view.findViewById(R.id.item_change_mydata_sure);
                    final TextView getCode= (TextView) view.findViewById(R.id.item_change_mydata_code);
                    getCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(phoneNum.getText().toString().trim().equals("")){
                                mToast.showText(getApplicationContext(),"手机号码不能为空");
                            }else {
                                getCode.setEnabled(false);
                                TimeCount(getCode,60*1000,1000);
                                mToast.showText(getApplicationContext(),"已发送");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getRequestCode(phoneNum.getText().toString().trim());
                                    }
                                }).start();
                            }
                        }
                    });
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(phoneNum.getText().toString().trim().equals("")){
                                mToast.showText(getApplicationContext(),"手机号码不能为空");
                            }else {
                                if(code.getText().toString().trim().equals("")){
                                    mToast.showText(getApplicationContext(),"验证码不能为空");
                                }else {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            sendInfoToServer(code.getText().toString().trim());
                                        }
                                    }).start();
                                    dialog.dismiss();
                                }
                            }

                        }
                    });
                    dialog.setContentView(view);
                    dialog.show();
                }
                break;
            case R.id.teacher_mydata_header:
                //修改头像
                addMenu();
                break;
            case R.id.take_photo:
                //打开相机
                if(mcheckPermission()){
                    camera();
                }
                break;
            case R.id.open_gallery:
                //打开相册
                gallery();
                break;
            case R.id.teacher_mydata_sex:
                AlertDialog.Builder builder=new AlertDialog.Builder(Mydata.this);
                builder.setItems(strSex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            sex.setText("男");
                            msex="1";
                            dialog.dismiss();
                        }else if(which==1){
                            sex.setText("女");
                            msex="2";
                            dialog.dismiss();
                        }
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
                break;
        }
    }

    private boolean mcheckPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int checkPermission_WR= ContextCompat.checkSelfPermission(Mydata.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int checkPermission_CAMERA=ContextCompat.checkSelfPermission(Mydata.this,Manifest.permission.CAMERA);
            if(checkPermission_WR!= PackageManager.PERMISSION_GRANTED && checkPermission_CAMERA!=PackageManager.PERMISSION_GRANTED){
                //没有授权,请求授权
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},1);
                return false;
            }else {
                return true;
            }
        }else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    camera();
                }else {
                    mcheckPermission();
                    mToast.showText(getApplicationContext(),"没有权限使用相机和存储文件！");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void getRequestCode(String phoneNum) {
        Request<JSONObject> getcode=NoHttp.createJsonObjectRequest(student.getCode(),RequestMethod.GET);
        getcode.add("type",2);
        getcode.add("id",teacher.teacherId(Mydata.this));
        getcode.add("phone",phoneNum);
        getRequestQueue().add(3, getcode, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    Message msg=new Message();
                    msg.what=1122;
                    handler.sendMessage(msg);
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

    private void TimeCount (final TextView getCode, long TotalTime, long intervalTime){
        CountDownTimer time=new CountDownTimer(TotalTime,intervalTime) {
            @Override
            public void onTick(long millisUntilFinished) {
                getCode.setText((millisUntilFinished/1000)+"秒");
            }

            @Override
            public void onFinish() {
                getCode.setEnabled(true);
                getCode.setText("获取验证码");
            }
        };
        time.start();
    }

    private void sendInfoToServer(String mcode) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherChangeData(),RequestMethod.POST);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add("code",mcode);
        request.add("mobile",phone_Num.getText().toString().trim());
        request.add("address",address.getText().toString().trim());
        request.add("qqnum",qq.getText().toString().trim());
        request.add("wechatnum",wechat.getText().toString().trim());
        request.add("email",email.getText().toString().trim());
        request.add("occupationno",id.getText().toString().trim());
        request.add("car_id",carInfo.getText().toString().trim());
        String[] ele=price.getText().toString().trim().split("元");
        request.add("subject_price",ele[0]);
        request.add("sex",msex);
        //上传头像,报错
        request.add("file", ImageCompress.bitmapToString(mCropImageFile));
        getRequestQueue().add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.e("response","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString().trim());
                    mmsg=object.getString("msg");
                    handler.sendEmptyMessage(3333);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                Message msg=new Message();
                msg.what=9999;
                handler.sendMessage(msg);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private boolean isRight() {
        boolean mphone=false,madd=false,mqq=false,mwc=false,mema=false,mid=false,mcar=false,mtrain=false;

        if(phone_Num.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"手机号码不能为空",Toast.LENGTH_SHORT).show();
        }else {
            mphone=true;
        }
        if(address.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"家庭地址不能为空",Toast.LENGTH_SHORT).show();
        }else {
            madd=true;
        }
        if(qq.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"QQ不能为空",Toast.LENGTH_SHORT).show();
        }else {
            mqq=true;
        }
        if(wechat.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"微信不能为空",Toast.LENGTH_SHORT).show();
        }else {
            mwc=true;
        }
        if(email.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"e-mail不能为空",Toast.LENGTH_SHORT).show();
        }else {
            mema=true;
        }
        if(id.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"教练证不能为空",Toast.LENGTH_SHORT).show();
        }else {
            mid=true;
        }
        if(carInfo.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"车辆信息不能为空",Toast.LENGTH_SHORT).show();
        }else {
            mcar=true;
        }
        if(price.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"课程单价不能为空",Toast.LENGTH_SHORT).show();
        }else {
            mtrain=true;
        }

        if(mphone&&madd&&mqq&&mwc&&mema&&mid&&mcar&&mtrain){
            return true;
        }else {
            return false;
        }

    };

    private void addMenu() {
        dialog=new BottomSheetDialog(this,R.style.ChoiceHeadTheme);
        View menu=getLayoutInflater().inflate(R.layout.item_choice_headerphoto_menu,null);
        Button takePhoto= (Button) menu.findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(this);
        Button open_gallery= (Button) menu.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(this);
        dialog.setContentView(menu);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void camera(){
        dialog.dismiss();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            mTmpFile = new File(FileUtils.createRootPath(getBaseContext()) + "/" + System.currentTimeMillis() + ".jpg");
            FileUtils.createFile(mTmpFile);
            if(Build.VERSION.SDK_INT < 24){
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mTmpFile));
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }else {
                //适配安卓7.0
                ContentValues contentValues=new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA,mTmpFile.getAbsolutePath());
                Uri uri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                grantUriPermission("com.kangzhan.student",uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }
        }
    }

    private void gallery(){
        dialog.dismiss();
        Intent intent = new Intent();
        //打开能打开相册的应用
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CAMERA){
            if (resultCode == RESULT_OK){
                crop(mTmpFile.getAbsolutePath());
            }else {
                Toast.makeText(getApplicationContext(), "拍照失败", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode==REQUEST_CROP){
            if (resultCode == RESULT_OK){
                mLog.e("设置头像","->OK");
                header.setImageURI(Uri.fromFile(mCropImageFile));
            }else {
                Toast.makeText(getApplicationContext(), "截图失败", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode==REQUEST_GALLERY){
            if (resultCode == RESULT_OK && data != null){
                String imagePath = handleImage(data);
                crop(imagePath);
            }else {
                Toast.makeText(getApplicationContext(), "打开图库失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void crop(String imagePath){
        //mCropImageFile = FileUtils.createTmpFile(getBaseContext());
        mCropImageFile = getmCropImageFile();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(new File(imagePath)), "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropImageFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CROP);
    }

    //把fileUri转换成ContentUri
    public Uri getImageContentUri(File imageFile){
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    private String handleImage(Intent data) {
        Uri uri = data.getData();
        String imagePath = null;
        if (Build.VERSION.SDK_INT >= 19) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                            "content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equals(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            }
        } else {
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }


    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //获取裁剪的图片保存地址
    private File getmCropImageFile(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"temp.jpg");
            File file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            return file;
        }
        return null;
    }

}
