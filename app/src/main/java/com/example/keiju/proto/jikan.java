package com.example.keiju.proto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Keiju on 2016/04/30.
 */
public class jikan extends Activity {
    private WebView mWebView;
    Handler hd;
    String strd;
    ListView listView;
    ListItem item;
    ArrayAdapter adapter;
    SharedPreferences sp;
    boolean sirabasu = false;
    boolean reload=false;
    List<ListItem> list = new ArrayList<ListItem>();
    int imagedraw[][] = {{R.drawable.getu1, R.drawable.getu2, R.drawable.getu3, R.drawable.getu4, R.drawable.getu5},
            {R.drawable.ka1, R.drawable.ka2, R.drawable.ka3, R.drawable.ka4, R.drawable.ka5,},
            {R.drawable.sui1, R.drawable.sui2, R.drawable.sui3, R.drawable.sui4, R.drawable.sui5},
            {R.drawable.moku1, R.drawable.moku2, R.drawable.moku3, R.drawable.moku4, R.drawable.moku5},
            {R.drawable.kin1, R.drawable.kin2, R.drawable.kin3, R.drawable.kin4, R.drawable.kin5}};

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_jikan);
        sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);

        int a;
        hd = new Handler();
        mWebView = (WebView) findViewById(R.id.jikan);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setSaveFormData(false);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new WebViewLogger(), "webViewLogger");
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setVisibility(GONE);
        mWebView.loadUrl("https://spoon.adm.konan-u.ac.jp/up/faces/up/km/Kma00401A.jsp");

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype, long contentLength) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType(mimetype);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });


        // adapterのインスタンスを作成
        adapter =
                new Custom(this, R.layout.row, list);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sirabasu = true;
                String string = sp.getString(String.valueOf(position), "0");
                Log.d("a", string);
                mWebView.evaluateJavascript("javascript:openSyllabus('" + string + "')", null);
            }
        });

    }

    public boolean onKeyDown(int KeyCode, KeyEvent event) {
        if (KeyCode == KeyEvent.KEYCODE_BACK && sirabasu == true) {
            mWebView.setVisibility(GONE);
            mWebView.loadUrl("https://spoon.adm.konan-u.ac.jp/up/faces/up/km/Kma00401A.jsp");
            sirabasu = false;
            reload=false;
        }else {
            return super.onKeyDown(KeyCode, event);
        }
        return true;
        }

    public class ListItem {

        private int imageId;
        private String text;

        public int getImageId() {
            return imageId;
        }

        public void setImageId(int imageId) {
            this.imageId = imageId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


    private class MyWebViewClient extends WebViewClient {


        @Override
        public void onPageFinished(WebView view, final String url) {
            if (listView.getCount() == 0)
                mWebView.evaluateJavascript("javascript:window.webViewLogger.log(document.documentElement.outerHTML);", null);
            if (sirabasu == true && reload==false) {
                reload=true;
               new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.setVisibility(View.VISIBLE);
                    }
                }, 500);
            }
        }
    }

    class WebViewLogger {
        @JavascriptInterface
        public void log(String str) {
            SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            int i = 0, j = 0, count = 0;
            String data = str;
            int tmp1 = 0, tmp2 = 0, tmp3 = 0, tmp4 = 0, tmp5 = 0;
            String jikanwari[] = {"getu", "ka", "sui", "moku", "kin", "do"};
            for (i = 0; i <= 5; i++) {
                for (j = 0; j <= 7; j++) {
                    ed.putString(jikanwari[i] + j, jikanwari[i] + j);
                    tmp1 = data.indexOf("form1:calendarList:" + i + ":rowVal0" + j);
                    tmp2 = data.indexOf("</span>", tmp1);
                    tmp3 = data.indexOf("</a>", tmp1);
                    tmp5 = data.indexOf(")", tmp1);
                    if (tmp2 - tmp1 > 50) {
                        ed.putString(String.valueOf(count), data.substring(tmp1 + 87, tmp5 -1));
                        strd = data.substring(tmp1 + 118, tmp3);
                        tmp4 = strd.indexOf("&nbsp;");
                        strd = strd.substring(0, tmp4);
                        ed.putString("line" + i + "row" + j, strd);
                        count++;
                    }
                }
            }

            ed.commit();


            hd.post(new Runnable() {
                int i = 0, j = 0;
                String tmp;
                SharedPreferences sp = getSharedPreferences("SAVEDATA", MODE_PRIVATE);


                @Override
                public void run() {
                    for (i = 0; i <= 5; i++) {
                        for (j = 1; j <= 7; j++) {
                            if (sp.getString("line" + i + "row" + j, "").length() < 70 && sp.getString("line" + i + "row" + j, "").length() > 1) {
                                item = new ListItem();
                                item.setText(sp.getString("line" + i + "row" + j, ""));
                                if (i == 0) {
                                    if (j == 1)
                                        item.setImageId(R.drawable.getu1);
                                    if (j == 2)
                                        item.setImageId(R.drawable.getu2);
                                    if (j == 3) {
                                        item.setImageId(R.drawable.getu3);
                                    }
                                    if (j == 4)
                                        item.setImageId(R.drawable.getu4);
                                    if (j == 5)
                                        item.setImageId(R.drawable.getu5);
                                    if (j == 6)
                                        item.setImageId(R.drawable.red);
                                    if (j == 7)
                                        item.setImageId(R.drawable.red);
                                } else if (i == 1) {
                                    if (j == 1)
                                        item.setImageId(R.drawable.ka1);
                                    if (j == 2)
                                        item.setImageId(R.drawable.ka2);
                                    if (j == 3)
                                        item.setImageId(R.drawable.ka3);
                                    if (j == 4)
                                        item.setImageId(R.drawable.ka4);
                                    if (j == 5)
                                        item.setImageId(R.drawable.ka5);
                                    if (j == 6)
                                        item.setImageId(R.drawable.red);
                                    if (j == 7)
                                        item.setImageId(R.drawable.red);
                                } else if (i == 2) {
                                    if (j == 1)
                                        item.setImageId(R.drawable.sui1);
                                    if (j == 2)
                                        item.setImageId(R.drawable.sui2);
                                    if (j == 3)
                                        item.setImageId(R.drawable.sui3);
                                    if (j == 4)
                                        item.setImageId(R.drawable.sui4);
                                    if (j == 5)
                                        item.setImageId(R.drawable.sui5);
                                    if (j == 6)
                                        item.setImageId(R.drawable.red);
                                    if (j == 7)
                                        item.setImageId(R.drawable.red);
                                } else if (i == 3) {
                                    if (j == 1)
                                        item.setImageId(R.drawable.moku1);
                                    if (j == 2)
                                        item.setImageId(R.drawable.moku2);
                                    if (j == 3)
                                        item.setImageId(R.drawable.moku3);
                                    if (j == 4)
                                        item.setImageId(R.drawable.moku4);
                                    if (j == 5)
                                        item.setImageId(R.drawable.moku5);
                                    if (j == 6)
                                        item.setImageId(R.drawable.red);
                                    if (j == 7)
                                        item.setImageId(R.drawable.red);
                                } else if (i == 4) {
                                    if (j == 1)
                                        item.setImageId(R.drawable.kin1);
                                    if (j == 2)
                                        item.setImageId(R.drawable.kin2);
                                    if (j == 3)
                                        item.setImageId(R.drawable.kin3);
                                    if (j == 4)
                                        item.setImageId(R.drawable.kin4);
                                    if (j == 5)
                                        item.setImageId(R.drawable.kin5);
                                    if (j == 6)
                                        item.setImageId(R.drawable.red);
                                    if (j == 7)
                                        item.setImageId(R.drawable.red);
                                } else if (i == 5) {
                                    if (j == 1)
                                        item.setImageId(R.drawable.red);
                                    if (j == 2)
                                        item.setImageId(R.drawable.red);
                                    if (j == 3)
                                        item.setImageId(R.drawable.red);
                                    if (j == 4)
                                        item.setImageId(R.drawable.red);
                                    if (j == 5)
                                        item.setImageId(R.drawable.red);
                                    if (j == 6)
                                        item.setImageId(R.drawable.red);
                                    if (j == 7)
                                        item.setImageId(R.drawable.red);
                                }

                                list.add(item);
                                adapter.notifyDataSetChanged();
                            }
                            //jikanlist.add(sp.getString("line" + i + "row" + j, ""));
                            //adapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

    }

    public class Custom extends ArrayAdapter<ListItem> {

        private int resourceId;
        private List<ListItem> items;
        private LayoutInflater inflater;

        public Custom(Context context, int resourceId, List<ListItem> items) {
            super(context, resourceId, items);

            this.resourceId = resourceId;
            this.items = items;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                view = this.inflater.inflate(this.resourceId, null);
            }

            ListItem item = this.items.get(position);

            // テキストをセット
            TextView appInfoText = (TextView) view.findViewById(R.id.row_textview);
            appInfoText.setText(item.getText());

            // アイコンをセット
            ImageView appInfoImage = (ImageView) view.findViewById(R.id.jikanwarigazou);
            appInfoImage.setImageResource(item.getImageId());

            return view;
        }
    }

}