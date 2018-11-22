package com.kangzhan.student.Student;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.kangzhan.student.BuildConfig;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.mainFragment;
import com.kangzhan.student.Student.Fragment.BookingFrqagment;
import com.kangzhan.student.Student.Fragment.ExamFragment;
import com.kangzhan.student.Student.Fragment.NewsFragment;
import com.kangzhan.student.Student.Fragment.TrainFragment;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.News.AddAdviseActivity;
import com.kangzhan.student.Student.person_data_activity.BookingRecordActivity;
import com.kangzhan.student.Student.person_data_activity.ChangePasswordActivity;
import com.kangzhan.student.Student.person_data_activity.ExamReusltActivity;
import com.kangzhan.student.Student.person_data_activity.LearnHourActivity;
import com.kangzhan.student.Student.person_data_activity.MyPurseActivity;
import com.kangzhan.student.Student.person_data_activity.PersondataActivity;
import com.kangzhan.student.Student.person_data_activity.TestResultActivity;
import com.kangzhan.student.Student_Login;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.kangzhan.student.utils.FileUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;

public class Student_MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{
    //用户姓名，Id，和头像
    private TextView user_name,user_id;
    private CircleImageView user_header;
    public static final int REQUEST_CODE = 111;
    private  DrawerLayout drawer;
    //
    private ViewPager mainViewPager;
    private TextView booking_tv,train_tv,exam_tv,news_tv;
    private ImageView booking_btn,train_btn,exam_btn,news_btn;
    //更换头像
    private File mTmpFile;
    private File mCropImageFile;
    private static final int     REQUEST_CAMERA = 100;
    private static final int     REQUEST_GALLERY = 101;
    private static final int     REQUEST_CROP = 102;
    private BottomSheetDialog dialog;
    //
    private String mmsg;
    private RelativeLayout r1,r2,r3,r4;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Student_MainActivity.this,"同步中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            AlertDialog.Builder buildler=new AlertDialog.Builder(Student_MainActivity.this);
                            buildler.setMessage(mmsg);
                            buildler.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            buildler.create().show();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(Student_MainActivity.this,"网络连接异常，请检测网络");
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         ZXingLibrary.initDisplayOpinion(Student_MainActivity.this);
         initView();
        initPagerView();drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.stu_nav_view);
        View headerLayout=navigationView.getHeaderView(0);
         user_header= (CircleImageView) headerLayout.findViewById(R.id.student_header);
         Glide.with(Student_MainActivity.this).load(student.studentPhotoUrl(Student_MainActivity.this)).error(R.drawable.header).into(user_header);
         mLog.e("photo","->"+student.studentPhotoUrl(this));
         user_header.setOnClickListener(this);
         user_id= (TextView) headerLayout.findViewById(R.id.student_id);
         user_id.setText(student.studentName(this)+"");
        navigationView.setNavigationItemSelectedListener(this);
    }
    //初始化导航按钮
    private void initView() {
        booking_tv= (TextView) findViewById(R.id.booking_tv);
        booking_btn= (ImageView) findViewById(R.id.booking_btn);
        r1= (RelativeLayout) findViewById(R.id.student_r1);
        r1.setOnClickListener(this);
        train_tv= (TextView) findViewById(R.id.train_tv);
        train_btn= (ImageView) findViewById(R.id.train_btn);
        r2= (RelativeLayout) findViewById(R.id.student_r2);
        r2.setOnClickListener(this);
        exam_tv= (TextView) findViewById(R.id.exam_tv);
        exam_btn= (ImageView) findViewById(R.id.exam_btn);
        r3= (RelativeLayout) findViewById(R.id.student_r3);
        r3.setOnClickListener(this);
        mainViewPager= (ViewPager) findViewById(R.id.content_main_viewPager);
        news_tv= (TextView) findViewById(R.id.news_tv);
        news_btn= (ImageView) findViewById(R.id.news_btn);
        r4= (RelativeLayout) findViewById(R.id.student_r4);
        r4.setOnClickListener(this);
        //

    }

    //初始化ViewPager
    private void initPagerView() {
        ArrayList<Fragment> fragmentsList=new ArrayList<>();
        fragmentsList.add(new BookingFrqagment());
        fragmentsList.add(new TrainFragment());
        fragmentsList.add(new ExamFragment());
        fragmentsList.add(new NewsFragment());
        mainFragment fragmentAdapter=new mainFragment(getSupportFragmentManager(),fragmentsList);
        mainViewPager.setAdapter(fragmentAdapter);
        mainViewPager.setCurrentItem(0);
        setButtonAndTextViewChange();
        booking_btn.setBackgroundResource(R.mipmap.booking1);
        booking_tv.setTextColor(ContextCompat.getColor(Student_MainActivity.this,R.color.textColor_evaluation));
        mainViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        setButtonAndTextViewChange();
                        booking_btn.setBackgroundResource(R.mipmap.booking1);
                        booking_tv.setTextColor(ContextCompat.getColor(Student_MainActivity.this,R.color.textColor_evaluation));
                        break;
                    case 1:
                        setButtonAndTextViewChange();
                        train_btn.setBackgroundResource(R.mipmap.train1);
                        train_tv.setTextColor(ContextCompat.getColor(Student_MainActivity.this,R.color.textColor_evaluation));
                        break;
                    case 2:
                        setButtonAndTextViewChange();
                        exam_btn.setBackgroundResource(R.mipmap.exam1);
                        exam_tv.setTextColor(ContextCompat.getColor(Student_MainActivity.this,R.color.textColor_evaluation));
                        break;
                    case 3:
                        setButtonAndTextViewChange();
                        news_btn.setBackgroundResource(R.mipmap.info1);
                        news_tv.setTextColor(ContextCompat.getColor(Student_MainActivity.this,R.color.textColor_evaluation));
                        break;
                    default:
                        setButtonAndTextViewChange();
                        booking_btn.setBackgroundResource(R.mipmap.booking1);
                        booking_tv.setTextColor(ContextCompat.getColor(Student_MainActivity.this,R.color.textColor_evaluation));
                        break;
                }
            }
        });
    }
    private void setButtonAndTextViewChange(){
        booking_btn.setBackgroundResource(R.mipmap.booking0);
        booking_tv.setTextColor(ContextCompat.getColor(Student_MainActivity.this,R.color.textColor_gray));
        train_btn.setBackgroundResource(R.mipmap.train0);
        train_tv.setTextColor(ContextCompat.getColor(Student_MainActivity.this,R.color.textColor_gray));
        exam_btn.setBackgroundResource(R.mipmap.exam0);
        exam_tv.setTextColor(ContextCompat.getColor(Student_MainActivity.this,R.color.textColor_gray));
        news_btn.setBackgroundResource(R.mipmap.info0);
        news_tv.setTextColor(ContextCompat.getColor(Student_MainActivity.this,R.color.textColor_gray));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.student_header:
                //修改头像
                addMenu();
                break;
            case R.id.take_photo:
                //打开相机
                camera();
                break;
            case R.id.open_gallery:
                //打开相册
                gallery();
                break;*/
            case R.id.student_r1:
                mainViewPager.setCurrentItem(0,false);
                break;
            case R.id.student_r2:
                mainViewPager.setCurrentItem(1,false);
                break;
            case R.id.student_r3:
                mainViewPager.setCurrentItem(2,false);
                break;
            case R.id.student_r4:
                mainViewPager.setCurrentItem(3,false);
                break;
            default:
                break;
        }
    }

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                //适配安卓7.0
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".provider", mTmpFile));
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.teacher_scan:
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                    int checkPermissions=ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                    if(checkPermissions!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
                    }else {
                        Analyze2DCode();
                    }
                }else {
                    Analyze2DCode();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Analyze2DCode();
                }else {
                    mToast.showText(getApplicationContext(),"请授权使用摄像头！");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void Analyze2DCode() {
        Intent intent = new Intent(Student_MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("您是要退出吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                //清除别名
                JPushInterface.setAlias(Student_MainActivity.this,"DEV_STU_"+"",null);
                finish();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.create().show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    final String result = bundle.getString(CodeUtils.RESULT_STRING);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendRequest("http://"+result);
                        }
                    }).start();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    mToast.showText(Student_MainActivity.this,"解析二维码失败");
                }
            }
        }else if(requestCode==REQUEST_CAMERA){
            if (resultCode == RESULT_OK){
                crop(mTmpFile.getAbsolutePath());
            }else {
                Toast.makeText(this, "拍照失败", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode==REQUEST_CROP){
            if (resultCode == RESULT_OK){
                user_header.setImageURI(Uri.fromFile(mCropImageFile));
            }else {
                Toast.makeText(this, "截图失败", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode==REQUEST_GALLERY){
            if (resultCode == RESULT_OK && data != null){
                String imagePath = handleImage(data);
                crop(imagePath);
            }else {
                Toast.makeText(this, "打开图库失败", Toast.LENGTH_SHORT).show();
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
    private void sendRequest(String url) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url, RequestMethod.GET);
        request.add("key", student.studentKey(Student_MainActivity.this));
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    Message msg=new Message();
                    msg.what=1111;
                    handler.sendMessage(msg);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            //更改用户头像
            case R.id.student_mydata:
                Intent persondata=new Intent(this, PersondataActivity.class);
                startActivity(persondata);
                break;
            case R.id.student_mypurse:
                Intent mypurse=new Intent(this, MyPurseActivity.class);
                startActivity(mypurse);
                break;
            case R.id.student_bookingRecord:
                Intent bookingRecord=new Intent(this, BookingRecordActivity.class);
                startActivity(bookingRecord);
                break;
            case R.id.student_learnHour:
                Intent learnhour=new Intent(this, LearnHourActivity.class);
                startActivity(learnhour);
                break;
            case R.id.student_exam:
                Intent examresult=new Intent(this, ExamReusltActivity.class);
                startActivity(examresult);
                break;
            case R.id.student_test:
                Intent testresult=new Intent(this, TestResultActivity.class);
                startActivity(testresult);
                break;
            case R.id.student_advise:
                final Intent advise=new Intent(this, AddAdviseActivity.class);
                AlertDialog.Builder builder=new AlertDialog.Builder(Student_MainActivity.this);
                builder.setTitle("选择投诉对象");
                builder.setItems(new String[]{"教练", "驾校"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            advise.putExtra("who",0);
                            startActivity(advise);
                            dialog.dismiss();
                        }else if(which==1){
                            advise.putExtra("who",1);
                            startActivity(advise);
                            dialog.dismiss();
                        }
                    }
                });
                builder.create().show();
                break;
            case R.id.student_changePassword:
                Intent changepassword=new Intent(this, ChangePasswordActivity.class);
                startActivity(changepassword);
                break;
            case R.id.student_logout:
                //清除别名
                JPushInterface.setAlias(this,"DEV_STU_"+"",null);
                SharedPreferences sp=getSharedPreferences("studentKey",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("key","false");
                editor.apply();
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
