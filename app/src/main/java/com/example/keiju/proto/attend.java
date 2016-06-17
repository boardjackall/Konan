package com.example.keiju.proto;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Keiju on 2016/04/30.
 */
public class attend extends Activity {
    private WebView mWebView;
    Handler hd;
    TextView tv;
    int kaisuu=0;
    String strd, attendstr;
    ListView listView;
    ArrayAdapter<String> adapter;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_attend);
        int a;
        hd = new Handler();

        SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);
        mWebView = (WebView) findViewById(R.id.webattend);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setSaveFormData(false);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new WebViewLogger(), "webViewLogger");
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setVisibility(View.GONE);
        mWebView.loadUrl("https://spoon.adm.konan-u.ac.jp/up/faces/up/jg/Jga00201A.jsp");

        ListView listView = new ListView(this);
        setContentView(listView);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        listView.setAdapter(adapter);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, final String url) {
            mWebView.evaluateJavascript("javascript:window.webViewLogger.log(document.documentElement.outerHTML);", null);

        }
    }

    class WebViewLogger {
        @JavascriptInterface
        public void log(String str) {
            SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            int i = 0, j = 0;

            String data = str;
            int tmp1 = 0, tmp2 = 0, tmp3 = 0, tmp4 = 0,tmp5=0;
            data.replaceAll("\t","a");
             for(i=0; i<50; i++) {
                 tmp1 = data.indexOf("td class=" + '"' + "kamoku" + '"', tmp1 + 1);
                 if(tmp1==-1)
                     break;
                 tmp4 = data.indexOf("（",tmp1+20);
                 tmp2 = data.indexOf("ritu", tmp1);
                 tmp3 = data.indexOf("</td>", tmp2);
                 strd = data.substring(tmp1+47,tmp4);
                 attendstr = data.substring(tmp2 + 6, tmp3);
                 if (tmp3 - (tmp2 + 6) > 0 && tmp3 - (tmp2 + 6) < 30) {
                     ed.putString("attendname" + i, strd);
                     ed.putString("attend%"+i,attendstr);
                     kaisuu=i+1;
                 }
                     Log.d("a", strd+attendstr);
             }

            ed.commit();

            hd.post(new Runnable() {
                @Override
                public void run() {
                    int i, j;
                    SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);
                    for (i=0; i<kaisuu; i++){
                        adapter.add(sp.getString("attendname"+i,"")+":"+sp.getString("attend%"+i,""));
                    }
                }
            });
        }
    }
}
