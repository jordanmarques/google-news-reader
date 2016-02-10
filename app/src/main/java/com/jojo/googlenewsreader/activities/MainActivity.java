package com.jojo.googlenewsreader.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.articles.ArticleListAsyncTask;
import com.jojo.googlenewsreader.articles.ArticleList;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    public static ArticleList articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listView = (ListView) findViewById(R.id.listView);

        ArticleListAsyncTask task = new ArticleListAsyncTask(this, listView);
        task.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;

                Intent intent = new Intent(MainActivity.this, ArticleDetail.class);
                intent.putExtra("articleForDetailActivity", articleList.getArticles().get(itemPosition));
                startActivityForResult(intent, 0);
            }
        });
    }

    public static void setArticleList(ArticleList articleList) {
        MainActivity.articleList = articleList;
    }
}
