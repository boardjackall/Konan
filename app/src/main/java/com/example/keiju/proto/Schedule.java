package com.example.keiju.proto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Keiju on 2016/06/06.
 */

public class Schedule extends AppCompatActivity {
    WebView schedlueweb;

    public void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(R.layout.schedule);
        schedlueweb = (WebView) findViewById(R.id.scheduleweb);
        schedlueweb.loadUrl("https://spoon.adm.konan-u.ac.jp/up/faces/up/po/pPoa0503A.jsp");

        schedlueweb.getSettings().setSaveFormData(false);
        schedlueweb.getSettings().setLoadWithOverviewMode(true);
        schedlueweb.getSettings().setUseWideViewPort(true);
        schedlueweb.getSettings().setBuiltInZoomControls(true);
        schedlueweb.getSettings().setJavaScriptEnabled(true);

        schedlueweb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }
}
