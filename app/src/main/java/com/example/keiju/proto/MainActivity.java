package com.example.keiju.proto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.R.attr.data;
import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    WebView mWebview;
    WebView noLecture;
    WebView hokouLecture;
    String id, pass;
    boolean loginflug = true;
    boolean loginjudge = false;
    boolean logincount = false;
    int newcount = 0,newcounthokou=0, nolecturecount = 0;
    Handler hd;
    TextView newNoLecture,newHokou;
    Button btn_nolecture,btn_hokou;
    boolean hokoushow = false;
    boolean hokoushoed = false;
Runnable update;
    private static final String TAG = MainActivity.class.getSimpleName();

    // BackボタンPress時の有効タイマー
    private CountDownTimer keyEventTimer;

    // 一度目のBackボタンが押されたかどうかを判定するフラグ
    private boolean pressed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);
        mWebview = (WebView) findViewById(R.id.loginweb);
        noLecture = (WebView) findViewById(R.id.kyuukouweb);
        hokouLecture = (WebView) findViewById(R.id.hokouweb);
//webViewの設定でjavascriptを有効にする
        mWebview.getSettings().setJavaScriptEnabled(true);
        noLecture.getSettings().setJavaScriptEnabled(true);
        hokouLecture.getSettings().setJavaScriptEnabled(true);
        noLecture.loadUrl("https://spoon.adm.konan-u.ac.jp/up/faces/up/po/Poa00601A.jsp");
        mWebview.loadUrl("https://spoon.adm.konan-u.ac.jp/up/faces/up/po/Poa00601A.jsp");
        hokouLecture.loadUrl("https://spoon.adm.konan-u.ac.jp/up/faces/up/po/Poa00601A.jsp");
        mWebview.addJavascriptInterface(new MainActivity.WebViewLogger(), "webViewLogger");
        hokouLecture.addJavascriptInterface(new MainActivity.WebViewLogger2(), "webViewLogger2");
        mWebview.setVisibility(View.GONE);
        noLecture.setVisibility(GONE);
        hokouLecture.setVisibility(GONE);
        hokouLecture.setWebViewClient(new MyWebViewClient2());
        mWebview.setWebViewClient(new MyWebViewClient());
//noLecture.setWebViewClient(new noLectureCliand());

        hd = new Handler();

        newNoLecture = (TextView) findViewById(R.id.newNoLecture);
        newHokou = (TextView)findViewById(R.id.text_hokou);


        // onFinish(), onTick()
        keyEventTimer = new CountDownTimer(1000, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "call onTick method");
            }

            @Override
            public void onFinish() {
                pressed = false;
            }
        };


        Button attbtn = (Button) findViewById(R.id.attbtn);
        attbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, attend.class);
                startActivity(intent);
            }
        });

        Button jikan = (Button) findViewById(R.id.jikanwari);
        jikan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, jikan.class);
                startActivity(intent);
            }
        });

        Button downbtn = (Button) findViewById(R.id.btnd);
        downbtn.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View v) {
                                           Intent intent = new Intent(MainActivity.this, download.class);
                                           startActivity(intent);
                                       }
                                   }
        );

        Button btn_lib = (Button) findViewById(R.id.btn_library);
        btn_lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, library.class);
                startActivity(intent);
            }
        });

        Button btn_sch = (Button) findViewById(R.id.schedule_btn);
        btn_sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Schedule.class);
                startActivity(intent);
            }
        });

        btn_hokou = (Button)findViewById(R.id.hokou_btn);
        btn_hokou.setEnabled(false);

        btn_nolecture = (Button) findViewById(R.id.kyuukou_btn);
        btn_nolecture.setEnabled(false);
        btn_nolecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoLecture.class);
                mWebview.destroy();
                hokouLecture.destroy();
                noLecture.destroy();
                startActivity(intent);
            }
        });
        Toast.makeText(getBaseContext(), "ユーザーID:" + sp.getString("id", "") + "でログインしました", Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        // Backボタン検知
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (!pressed) {
                // Timerを開始
                keyEventTimer.cancel();
                keyEventTimer.start();

                // 終了する場合, もう一度タップするようにメッセージを出力する
                Toast.makeText(this, "終了する場合は、もう一度バックボタンを押してください", Toast.LENGTH_SHORT).show();
                pressed = true;
                return false;
            }

            // pressed=trueの時、通常のBackボタンで終了処理.

            return super.dispatchKeyEvent(event);
        }
        // Backボタンに関わらないボタンが押された場合は、通常処理.
        android.os.Process.killProcess(android.os.Process.myPid());
        return super.dispatchKeyEvent(event);
    }

    class WebViewLogger {
        @JavascriptInterface
        public void log(String str) {
            SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            int i = 0, j = 0, count = 0;
            String data = str;
            int tmp1 = 0, tmp2 = 0, tmp3 = 0, tmp4 = 0, tmp5 = 0, tmp6 = 0, tmp7 = 0, tmp8 = 0;
            tmp1 = data.indexOf("休講情報");
            tmp2 = data.indexOf(">全", tmp1);
            tmp3 = data.indexOf("件", tmp1);
            String a = str.substring(tmp2 + 2, tmp3);
            ed.putString("noLectureCount", a);
            tmp4 = tmp1;
            tmp7 = data.indexOf("休講", tmp4 + 50);

            while (tmp7 < tmp2) {
                tmp5 = data.indexOf("休講", tmp4 + 50);
                tmp6 = data.indexOf("「", tmp4 + 50);
                tmp4 = tmp5 + 50;
                ed.putString("noLecture" + nolecturecount, String.valueOf(data.substring(tmp6 - 5, tmp5)));
                tmp7 = data.indexOf("休講", tmp4 + 1);
                tmp8 = data.indexOf("form1:Poa00201A:htmlParentTable:2:htmlDetailTbl:" + nolecturecount + ":linkEx1", tmp8 + 1);
                ed.putString("noLecture" + nolecturecount, data.substring(tmp6 - 5, tmp5));
                ed.putString("noLectureLink" + nolecturecount, data.substring(tmp8, tmp8 + 57));
                nolecturecount++;
                ed.commit();
            }


            while (data.indexOf("yet.gif", tmp1) > tmp1 && data.indexOf("yet.gif", tmp1) < tmp3) {
                newcount++;
                tmp1 = data.indexOf("yet.gif", tmp1);
            }

            hd.post(new Runnable() {
                SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);

                @Override
                public void run() {
                    if(newcount>0)
                    newNoLecture.setText("未読:"+String.valueOf(newcount)+"件");
                    else
                    newNoLecture.setText("新着情報はありません");
                    btn_nolecture.setEnabled(true);
                }
            });
            ed.commit();
        }

    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, final String url) {
            SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);
            mWebview.evaluateJavascript("javascript:document.getElementById('form1:Poa00201A:htmlParentTable:1:htmlDisplayOfAll:0:allInfoLinkCommand').click();", null);

            hd.postDelayed(update,1000);
            update = new Runnable() {
                @Override
                public void run() {
                    mWebview.evaluateJavascript("javascript:window.webViewLogger.log(document.documentElement.outerHTML);", null);
                }
            };
        }
    }

    class WebViewLogger2 {
        @JavascriptInterface
        public void log2(String str) {
                SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                int tmp1, tmp2, tmp3, tmp4, tmp5, tmp6;
                tmp1 = tmp2 = tmp3 = tmp4 = tmp5 = tmp6 = 0;
                tmp1 = str.indexOf("form1:Poa00201A:htmlParentTable:0:htmlDetailTbl2:0:htmlMidokul2");
            if(str.indexOf("form1:Poa00201A:htmlParentTable:3:htmlDetailTbl:0:htmlMidokul")!=-1) {
                tmp1 = str.indexOf("form1:Poa00201A:htmlParentTable:3:htmlDetailTbl:0:htmlMidokul");
                while (str.indexOf("#information", tmp1 + 1) < str.indexOf("form1:Poa00201A:htmlParentTable:3:htmlDisplayOfAll:0:htmlCountCol21702")) {
                    tmp2 = str.indexOf("onclick", tmp1);
                    tmp3 = str.indexOf("title", tmp2);
                    tmp4=str.indexOf(">",tmp3);
                    if(str.indexOf("yet.gif",tmp1)<str.indexOf("form1:Poa00201A:htmlParentTable:3:htmlDisplayOfAll:0:htmlCountCol21702")) {
                        Log.d("count", String.valueOf(newcounthokou));
                    newcounthokou++;
                    }
                    String a = str.substring(tmp3, tmp4);
                    Log.d("a1", a);
                    tmp1 = tmp2 + 1;
                }
            }else{
tmp5=str.indexOf("src",tmp1);
                while (str.indexOf("#information", tmp1 + 1) > 0) {
                    tmp2 = str.indexOf("onclick", tmp1);
                    tmp3 = str.indexOf("title", tmp2);
                    tmp4=str.indexOf(">",tmp3);
                    if(str.indexOf("yet.gif",tmp5)<tmp2 && str.indexOf("yet.gif",tmp5)>tmp5){
                        Log.d("a2",String.valueOf(newcounthokou));
                        newcounthokou++;
                    }
                    tmp5=str.indexOf("src",tmp2);
                    String a = str.substring(tmp3+7, tmp4-1);
                    Log.d("a", a);
                    tmp1 = tmp2 + 1;
                }
            }
            hd.post(new Runnable() {
                @Override
                public void run() {
                    btn_hokou.setEnabled(true);
                    if(newcounthokou>0)
               newHokou.setText("未読:"+String.valueOf(newcounthokou)+"件");
                    else
                        newHokou.setText("新着情報はありません");
                }
            });
        }
    }

    private class MyWebViewClient2 extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, final String url) {
            SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);

            if(hokoushow==false) {
                hokouLecture.evaluateJavascript("javascript:document.getElementById('form1:Poa00201A:htmlParentTable:3:htmlDisplayOfAll:0:allInfoLinkCommand').click();", null);
                //hokouLecture.reload();
                hd.postDelayed(update,1000);
                update = new Runnable() {
                    @Override
                    public void run() {
                        hokouLecture.evaluateJavascript("javascript:window.webViewLogger2.log2(document.documentElement.outerHTML);", null);
                        hokoushow = true;
                    }
                };
            }
        }
    }
}
