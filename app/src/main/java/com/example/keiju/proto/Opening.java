package com.example.keiju.proto;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by Keiju on 2016/04/28.
 */
public class Opening extends Activity {
    WebView webView;
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_opening);    //オープニング用レイアウトを表示
        Handler hdl = new Handler();
        hdl.postDelayed(new openingHandler(), 2000);    //1秒(1000ms)後にclassを実行
    }

    class openingHandler implements Runnable {
        public void run() {
            SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);
                Intent intent = new Intent(getApplication(), Login.class);    //本編用のclass
                startActivity(intent);    //実行
                Opening.this.finish();    //オープニングは終了

        }
    }
}
