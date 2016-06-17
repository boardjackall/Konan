package com.example.keiju.proto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Keiju on 2016/06/05.
 */

public class sirabasu {
    public class library extends AppCompatActivity {
        WebView webview;

        public void onCreate(Bundle save) {
            super.onCreate(save);
            setContentView(R.layout.library);
            webview = (WebView)findViewById(R.id.libraryweb);
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.loadUrl("http://www.konan-u.ac.jp/lib/");
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setBuiltInZoomControls(true);

            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
        }
    }

}
