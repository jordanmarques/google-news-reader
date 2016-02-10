package com.jojo.googlenewsreader.articles;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.activities.MainActivity;
import com.jojo.googlenewsreader.pojo.Article;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArticleListAsyncTask extends AsyncTask<Void, Void, Void> {
    private String APIUrl = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0";
    private String query;
    private Context context;
    private ListView listView;
    private ArticleList articleList;

    public ArticleListAsyncTask(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }


    @Override
    protected void onPreExecute() {
        articleList = new ArticleList(new String[]{"Chargement en cours ..."}, new ArrayList());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, articleList.getListTitles());
        listView.setAdapter(adapter);
    }

    @Override
    protected Void doInBackground(Void... params) {
        query = "barack%20obama";
        try {
            JSONArray jsonString = getJsonFromServer(APIUrl+"&q=barack%20obama");
            String[] listTitles = new String[jsonString.length()];
            List<Article> articles = new ArrayList<>();
            for(int i = 0; i<jsonString.length(); i++) {
                articles.add(new Article(jsonString.getJSONObject(i).getString("title"),
                        jsonString.getJSONObject(i).getString("content"),
                        jsonString.getJSONObject(i).getJSONObject("image").getString("url"),
                        jsonString.getJSONObject(i).getString("url")));

                listTitles[i] = jsonString.getJSONObject(i).getString("title");
            }
            articleList = new ArticleList(listTitles, articles);
        }
        catch (IOException | JSONException error) {
            error.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        MainActivity.setArticleList(articleList);
        ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(context, R.layout.article_line, articleList.getArticles());
        listView.setAdapter(arrayAdapter);
    }

    public static JSONArray getJsonFromServer(String url) throws IOException {

        BufferedReader inputStream = null;

        URL jsonUrl = new URL(url);
        URLConnection dc = jsonUrl.openConnection();

        dc.setConnectTimeout(5000);
        dc.setReadTimeout(5000);

        inputStream = new BufferedReader(new InputStreamReader(dc.getInputStream()));

        // read the JSON results into a string
        String jsonResult = inputStream.readLine();
        try {
            JSONObject json = new JSONObject(jsonResult); // convert String to JSONObject
            json = json.getJSONObject("responseData");
            JSONArray titles = json.getJSONArray("results");
            return titles;
        }
        catch (JSONException error) {
            System.out.println(error);
            return (null);
        }
    }
}