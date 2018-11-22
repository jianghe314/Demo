package com.kangzhan.student.Advisetment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;

public class RecommendDetailActivity extends BaseActivity {
    private WebView webView;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_news_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.recommend_news_detail_toolbar);
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
        webView= (WebView) findViewById(R.id.webView);
        WebSettings setting=webView.getSettings();
        setting.setJavaScriptEnabled(true);     //http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/student_consultDec.html?
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        setting.setLoadsImagesAutomatically(true); //支持自动加载图片
        setting.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    private void initData() {
        webView.loadUrl("http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/"+url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //开始加载
                showProgress.showProgress(RecommendDetailActivity.this,"加载中...");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl("http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/"+url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //加载结束
                showProgress.closeProgress();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.clearFormData();
        webView.clearHistory();
        webView.clearCache(true);
        webView.destroy();
    }
}
