package com.jojo.googlenewsreader.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jojo.googlenewsreader.R;

public class WebView extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        String urlArticle = getIntent().getStringExtra(ArticleDetail.urlArticle);

        android.webkit.WebView webView = (android.webkit.WebView) findViewById(R.id.webView);
        webView.loadUrl(urlArticle);

//        String summary = "<html><body>You scored <b>192</b> points.</body></html>";
        webView.loadData(urlArticle, "hmtl", null);


//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);



    }
}
