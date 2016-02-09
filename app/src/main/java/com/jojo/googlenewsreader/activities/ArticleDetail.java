package com.jojo.googlenewsreader.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.pojo.Article;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticleDetail extends AppCompatActivity {

    private Article article;
    public static String urlArticle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article_detail);

        this.article = (Article) getIntent().getSerializableExtra("articleForDetailActivity");

        TextView title = (TextView)findViewById(R.id.title);
        TextView description = (TextView)findViewById(R.id.description);

        title.setText(article.getTitle());
        description.setText(article.getContent());


        Button button = (Button) findViewById(R.id.openWebView);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ArticleDetail.this, WebView.class);
//                try {
//                    intent.putExtra(urlArticle, article.getString("url"));
//                }
//                catch (JSONException error) {
//
//                }
                startActivity(intent);
            }
        });
    }
}
