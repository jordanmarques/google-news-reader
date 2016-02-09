package com.jojo.googlenewsreader.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jojo.googlenewsreader.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticleDetail extends AppCompatActivity {

    protected JSONObject article;
    public static String urlArticle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        final Intent intent = getIntent();
        try {
            this.article = new JSONObject(intent.getStringExtra(Home.selectedArticle));
            String titre = this.article.getString("title");
            TextView title = (TextView)findViewById(R.id.title);
            title.setText(titre);
            TextView description = (TextView)findViewById(R.id.description);
            description.setText(this.article.getString("content"));
        }
        catch (JSONException error) {

        }
        Button button = (Button) findViewById(R.id.openWebView);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ArticleDetail.this, WebView.class);
                try {
                    intent.putExtra(urlArticle, article.getString("url"));
                }
                catch (JSONException error) {

                }
                startActivity(intent);
            }
        });
    }
}
