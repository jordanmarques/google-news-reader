package com.jojo.googlenewsreader.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jojo.googlenewsreader.R;

public class WebView extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);
        String urlArticle = getIntent().getStringExtra(ArticleDetail.urlArticle);
        android.webkit.WebView webView = (android.webkit.WebView) findViewById(R.id.webView);
        webView.loadUrl(urlArticle);
    }
}
