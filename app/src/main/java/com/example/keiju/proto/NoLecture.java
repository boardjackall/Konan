package com.example.keiju.proto;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Keiju on 2016/06/06.
 */

public class NoLecture extends AppCompatActivity {
    private ListView lv;
    boolean nolectureshow=false;
    Handler hd;
   private WebView nolectureweb;
    SharedPreferences sp;
    int i=0, j;

    @Override
    public void onCreate(Bundle save) {
        super.onCreate(save);
        sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);
        setContentView(R.layout.activity_kyuukou);

        nolectureweb = (WebView)findViewById(R.id.nolectureweb);
        nolectureweb.getSettings().setSaveFormData(false);
        nolectureweb.getSettings().setLoadWithOverviewMode(true);
        nolectureweb.getSettings().setUseWideViewPort(true);
        nolectureweb.getSettings().setBuiltInZoomControls(true);
        nolectureweb.getSettings().setJavaScriptEnabled(true);


        
        //adapterのインスタンスを作成
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        while (new Integer(sp.getString("noLectureCount", "0")).intValue() > i) {
            adapter.add(sp.getString("noLecture" + i, ""));
            i++;
        }

        lv = (ListView) findViewById(R.id.nolecturelist);
        //lv.setAdapter(adapter);

        nolectureweb.loadUrl("https://spoon.adm.konan-u.ac.jp/up/faces/up/po/Poa00601A.jsp");
        nolectureweb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view,String url){
                super.onPageFinished(view,url);
                if(nolectureshow==false)
                lv.setAdapter(adapter);
                if(nolectureshow==true){
                    nolectureweb.setVisibility(View.VISIBLE);
                }
            }
        });

        //リスト項目が選択された時のイベントを追加
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nolectureweb.loadUrl("https://spoon.adm.konan-u.ac.jp/up/faces/up/po/pPoa0202A.jsp?fieldId=form1:Poa00201A:htmlParentTable:2:htmlDetailTbl:0:linkEx1");
                nolectureshow=true;

            }
        });

        //リスト項目が長押しされた時のイベントを追加
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = position + "番目のアイテムが長押しされました";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    public boolean onKeyDown(int KeyCode, KeyEvent event) {
        if (KeyCode == KeyEvent.KEYCODE_BACK && nolectureshow==true ) {
            nolectureshow=false;
        }else {
            return super.onKeyDown(KeyCode, event);
        }
        return true;
    }
}
