package com.kangzhan.student;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kangzhan.student.Debug.mLog;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnicornImageLoader;
import com.qiyukf.unicorn.api.YSFOptions;
import com.yanzhenjie.nohttp.NoHttp;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by kangzhan011 on 2017/5/22.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //设置极光推送的Debug模式
        JPushInterface.setDebugMode(false);
        //初始化代码
        JPushInterface.init(this);
        mLog.e("jpush","初始化完成");
        //Nohttp的初始化
        NoHttp.initialize(this);

        //配置七鱼客服
        Unicorn.init(this, "22ed6fcfaca77dd3675bc6489d1bd1ce", option(), new UnicornImageLoader() {
            @Nullable
            @Override
            public Bitmap loadImageSync(String uri, int width, int height) {
                return null;
            }

            @Override
            public void loadImage(String uri, int width, int height, final ImageLoaderListener listener) {
                Glide.with(getApplicationContext()).load(uri).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if(listener!=null){
                            listener.onLoadComplete(resource);
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        if(listener!=null){
                            listener.onLoadFailed(e);
                        }
                    }
                });
            }
        });


    }
    private YSFOptions option(){
        YSFOptions options=new YSFOptions();
        options.statusBarNotificationConfig=new StatusBarNotificationConfig();
        options.savePowerConfig=new SavePowerConfig();
        return options;
    }



}
