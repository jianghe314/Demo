package com.kangzhan.student.HomeFragment.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.kangzhan.student.Advisetment.RecommendDetailActivity;
import com.kangzhan.student.HomeFragment.mDialog.WaitDialog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;

public class Home_Detail_URl extends BaseActivity {

    private WebView webView;
    private String url;
    private   WaitDialog wait;
    private RelativeLayout webview_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__detail__url);
        Toolbar toolbar= (Toolbar) findViewById(R.id.home_detail_url_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent data=getIntent();
        url=data.getStringExtra("url");
        initView();
        initData();
    }

    private void initView() {
        webview_container= (RelativeLayout) findViewById(R.id.webview_container);
        wait=new WaitDialog(this);
        webView=new WebView(getApplicationContext());
        WebSettings setting=webView.getSettings();
        setting.setJavaScriptEnabled(true);     //http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/student_consultDec.html?
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        setting.setLoadsImagesAutomatically(true); //支持自动加载图片
        setting.setDefaultTextEncodingName("utf-8");//设置编码格式
        webview_container.addView(webView);
    }


    private void initData() {
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //开始加载
                wait.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //加载结束
                wait.dismiss();
                wait.cancel();
            }
        });

    }




    @Override
    protected void onDestroy() {
        webView.removeAllViews();
        webView.clearFormData();
        webView.clearHistory();
        webView.clearCache(true);
        webView.destroy();
        webview_container.removeAllViews();
        super.onDestroy();
    }



}
