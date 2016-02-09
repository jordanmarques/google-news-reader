package com.jojo.googlenewsreader.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jojo.googlenewsreader.R;

public class WebView extends AppCompatActivity {

    private String urlArticle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Intent intent = getIntent();
        this.urlArticle = intent.getStringExtra(ArticleDetail.urlArticle);
        android.webkit.WebView webView = (android.webkit.WebView) findViewById(R.id.webView);
        System.out.println(this.urlArticle);
        webView.loadUrl(this.urlArticle);
    }
}
