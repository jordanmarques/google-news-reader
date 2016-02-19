package com.jojo.googlenewsreader.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.pojo.Article;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ArticleDetail extends ParentActivity {

    public static String urlArticle;
    public static Bitmap image;

    private Article article;
    private static final int PORTRAIT = 1;
    private static Button webViewButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == PORTRAIT){
            setContentView(R.layout.activity_article_detail);
        }
        else{
            setContentView(R.layout.activity_article_detail_land);
        }

        this.article = (Article) getIntent().getSerializableExtra("articleForDetailActivity");

        TextView title = (TextView)findViewById(R.id.title);
        TextView description = (TextView)findViewById(R.id.description);
        TextView publisher = (TextView)findViewById(R.id.textView2);
        TextView date = (TextView)findViewById(R.id.textView3);
        TextView url = (TextView)findViewById(R.id.url);
        ImageView imageViewLandscape = (ImageView)findViewById(R.id.imageViewLandscape);

        title.setText(Html.fromHtml(article.getTitle()));
        description.setText(Html.fromHtml(article.getContent()));
        publisher.setText(Html.fromHtml(article.getPublisher()));
        date.setText(Html.fromHtml(article.getDate()));
        imageViewLandscape.setImageBitmap(image);



        if (orientation == PORTRAIT){
            String articleUrl = article.getUrl();
            try {
                urlArticle = URLDecoder.decode(articleUrl, "UTF-8");
                url.setText(urlArticle);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        webViewButton = (Button) findViewById(R.id.webView_button);
        webViewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ArticleDetail.this, BrowserView.class);
                intent.putExtra(urlArticle, article.getUrl());
                startActivityForResult(intent, 0);
            }
        });

        Button backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void onNetworkChange(Boolean networkState){
        if(null != webViewButton){
            webViewButton.setEnabled(networkState);
        }
    }

    public static Bitmap getImage() {
        return image;
    }
    public static void setImage(Bitmap image) {
        ArticleDetail.image = image;
    }
}
