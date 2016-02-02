package com.jojo.googlenewsreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity {

    private JSONObject article;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final Intent intent = getIntent();
        try {
            this.article = new JSONObject(intent.getStringExtra(MainActivity.selectedArticle));
            String titre = this.article.getString("title");
            TextView title = (TextView)findViewById(R.id.title);
            title.setText(titre);
            TextView description = (TextView)findViewById(R.id.description);
            description.setText(this.article.getString("content"));
        }
        catch (JSONException error) {

        }
    }
}
