package com.jojo.googlenewsreader;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

public class Main22Activity extends AppCompatActivity {

    private String urlArticle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
        Intent intent = getIntent();
        this.urlArticle = intent.getStringExtra(Main2Activity.urlArticle);
        WebView webView = (WebView) findViewById(R.id.webView);
        System.out.println(this.urlArticle);
        webView.loadUrl(this.urlArticle);
    }
}
