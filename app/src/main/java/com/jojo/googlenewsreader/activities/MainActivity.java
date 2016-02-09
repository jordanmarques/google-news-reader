package com.jojo.googlenewsreader.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.articles.JSONAsyncTask;
import com.jojo.googlenewsreader.articles.ListArticles;
import com.jojo.googlenewsreader.pojo.Article;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity {

    public ListView viewById;
    public static ListArticles listArticles = new ListArticles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        JSONAsyncTask task = new JSONAsyncTask();
        task.execute();

        viewById = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listArticles.getListTitles());

        viewById.setAdapter(adapter);

        viewById.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;

                Intent intent = new Intent(MainActivity.this, ArticleDetail.class);
                intent.putExtra("articleForDetailActivity", listArticles.getArticles().get(itemPosition));
                startActivityForResult(intent, 0);
            }
        });
    }

    public ListView getViewById() {
        return viewById;
    }
    public void setViewById(ListView viewById) {
        this.viewById = viewById;
    }

    public static ListArticles getListArticles() {
        return listArticles;
    }
    public static void setListArticles(ListArticles listArticles) {
        MainActivity.listArticles = listArticles;
    }
}
