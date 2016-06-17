package com.example.keiju.proto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Keiju on 2016/05/08.
 */
public class download extends AppCompatActivity {
    WebView webview;
    public void onCreate(Bundle save){
        super.onCreate(save);
        setContentView(R.layout.activity_download);

        webview = (WebView)findViewById(R.id.web_download);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webview.loadUrl("http://www.navitime.co.jp/weather/poi?id=00209013");
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
    }
}
