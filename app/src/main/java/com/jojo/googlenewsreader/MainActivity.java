package com.jojo.googlenewsreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jojo.googlenewsreader.articles.ListArticles;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    public ListView viewById;
    public static ListArticles listArticles = new ListArticles();
    public static String selectedArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSONAsyncTask task = new JSONAsyncTask();
        task.execute();
        // Get ListView object from xml
        viewById = (ListView) findViewById(R.id.listView);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listArticles.listTitles);


        // Assign adapter to ListView
        viewById.setAdapter(adapter);

        // ListView Item Click Listener
        viewById.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                long itemIdAtPosition = viewById.getItemIdAtPosition(position);

                // Show Alert
                try {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.putExtra(selectedArticle, listArticles.articles.get(itemPosition).toString());
                    startActivityForResult(intent, 0);
                    //Toast.makeText(getApplicationContext(), "Position:"+itemPosition+"  ListItem : " +itemIdAtPosition+ "JSONContent :"+ listArticles.articles.get(itemPosition) , Toast.LENGTH_LONG).show();
                }
                catch (JSONException error) {

                }
            }
        });
    }
}
