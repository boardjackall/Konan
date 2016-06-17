package com.example.keiju.proto;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.renderscript.Element;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.xml.transform.Source;

/**
 * Created by Keiju on 2016/04/28.
 */
public class moji extends Activity {
    private WebView mWebview;
    String sample = "";

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.mojiretu);


        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            String TagetUrl = "https://spoon.adm.konan-u.ac.jp/up/faces/up/po/Poa00601A.jsp";//URLを変数へ
            String strLine;
            URL url = new URL(TagetUrl);
            Object content = url.getContent();
            if (content instanceof InputStream) {
                BufferedReader bf = new BufferedReader(new InputStreamReader
                        ((InputStream)content,"utf-8"));
                while ((strLine = bf.readLine()) != null) {
                    sample += strLine;
                }
            }
            else {
                System.out.println(content.toString());
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error");
            System.exit(-1);
        }
        catch (IOException e) {
            System.err.println(e);
            System.exit(-1);
        }

        String sample2 = sample.replaceAll("\t","");
        sample2 = sample2.replaceAll(" ","");
        int a = 0;
    }


}
