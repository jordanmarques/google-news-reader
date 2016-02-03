package com.jojo.googlenewsreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity {

    protected JSONObject article;
    public static String urlArticle;

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
        Button button = (Button) findViewById(R.id.openWebView);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main22Activity.class);
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
