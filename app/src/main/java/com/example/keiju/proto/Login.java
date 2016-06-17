package com.example.keiju.proto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
    WebView mWebview;
    String id, pass;
    Button login_btn;
    boolean loginchange = false;
    boolean loginflug = false;
    boolean loginjudge = false;
    boolean logincount = false;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sp = getSharedPreferences("SAVEDATA",MODE_PRIVATE);
        int judge = (Integer)sp.getInt("loginjudge",0);
        EditText idtext = (EditText)findViewById(R.id.idText);
        EditText passtext = (EditText)findViewById(R.id.passText);

        if (judge == 1) {
            if(sp.getString("id","") != idtext.toString())
                loginchange=true;
            idtext.setText(sp.getString("id",""));
            passtext.setText(sp.getString("password",""));
        }
        login_btn = (Button)findViewById(R.id.btn1);
        mWebview = (WebView) findViewById(R.id.webView);
        mWebview.setWebViewClient(new MyWebViewClient());
        mWebview.setVisibility(View.GONE);
//webViewの設定でjavascriptを有効にする
        mWebview.getSettings().setJavaScriptEnabled(true);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IDを取得
                if (checkInternetConnection()) {
                    EditText e = (EditText) findViewById(R.id.idText);
                    id = e.getText().toString();
                    //Passを取得
                    e = (EditText) findViewById(R.id.passText);
                    pass = e.getText().toString();
                    loginflug = true;
                    mWebview.loadUrl("https://spoon.adm.konan-u.ac.jp/up/faces/login/Com00505A.jsp");
                    progressDialog = new ProgressDialog(Login.this);
                    progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            return true;
                        }
                    });
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setIndeterminate(false);
                    progressDialog.setMessage("ログインしています");
                    progressDialog.setCancelable(true);
                    progressDialog.show();

                }
            }
        });

    }

    public boolean checkInternetConnection() {
        //端末がネットワークに繋がっているか確認する。
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);

        //ConnectivityManagerをもとに、通信の可否でtrue/falseを返す
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            Log.d("a", "NetworkInfo_isAvailable:" + info.isAvailable());
            Log.d("a", "NetworkInfo_isConnected:" + info.isConnected());
            Log.d("a", "NetworkInfo_isRoaming:" + info.isRoaming());
            if (info.isAvailable() && info.isConnected()) {
                return true;
            } else {
                return false;
            }
        } else {
            Toast.makeText(getBaseContext(),"ネットワークに繋がっていません",Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, final String url) {
            mWebview.evaluateJavascript("javascript:document.getElementById('form1:htmlUserId').value = '" + id + "';", null);
            mWebview.evaluateJavascript("javascript:document.getElementById('form1:htmlPassword').value = '" + pass + "';", null);



            if (loginflug == true) {
                mWebview.evaluateJavascript("javascript:document.getElementById('form1:login').click();", null);
            }


            if (url.equals(("https://spoon.adm.konan-u.ac.jp/up/faces/login/Com00505A.jsp"))) {
                if (logincount == true) {
                    SharedPreferences sp = getSharedPreferences("SAVEDATA",MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putInt("loginjudge",0);
                    ed.commit();
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "IDまたはパスワードが違います", Toast.LENGTH_LONG).show();
                    loginjudge = true;
                }
                logincount = true;
                if (loginjudge == true) {
                    logincount = false;
                    loginjudge = false;
                }
            }

            if (url.equals(("https://spoon.adm.konan-u.ac.jp/up/faces/up/po/Poa00601A.jsp"))) {
                //Preferencesを取得する
                SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();

                if(loginchange==true)
                ed.clear();

                ed.putString("id",id);
                ed.putString("password",pass);
                ed.putInt("loginjudge",1);
                ed.commit();
                loginjudge = true;
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                logincount=false;
                loginchange=false;
                finish();

            }
            loginflug = false;
        }
    }


}
