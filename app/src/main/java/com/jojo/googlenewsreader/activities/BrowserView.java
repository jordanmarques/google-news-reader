package com.jojo.googlenewsreader.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.jojo.googlenewsreader.R;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class BrowserView extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String urlArticle = getIntent().getStringExtra(ArticleDetail.urlArticle);
        try {
            urlArticle = URLDecoder.decode(urlArticle, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.setWebViewClient(new myWebClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(urlArticle);
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }
    }






}
