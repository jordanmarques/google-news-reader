package com.jojo.googlenewsreader.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.activities.MainActivity;
import com.jojo.googlenewsreader.articles.ArticleArrayAdapter;
import com.jojo.googlenewsreader.articles.ArticleList;
import com.jojo.googlenewsreader.pojo.Article;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadArticleAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String API_URL = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=";
    
    private String query;
    private Context context;
    private ListView listView;
    private ArticleList articleList;

    public LoadArticleAsyncTask(Context context, ListView listView, String query) {
        this.context = context;
        this.listView = listView;
        this.query = query;
    }

    public LoadArticleAsyncTask(Context context, ListView listView) {
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

        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        articleList = searchFromQuery(API_URL + query);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        MainActivity.setArticleList(articleList);
        ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(context, R.layout.article_line, articleList.getArticles());
        listView.setAdapter(arrayAdapter);
    }

    private ArticleList searchFromQuery(String query) {
        try {
            JSONArray jsonString = getJsonFromServer(query);
            String[] listTitles = new String[jsonString.length()];
            List<Article> articles = new ArrayList<>();
            for(int i = 0; i<jsonString.length(); i++) {
                articles.add(new Article(jsonString.getJSONObject(i).getString("title"),
                        jsonString.getJSONObject(i).getString("content"),
                        jsonString.getJSONObject(i).getJSONObject("image").getString("url"),
                        jsonString.getJSONObject(i).getString("url"),
                        jsonString.getJSONObject(i).getString("publisher"),
                        jsonString.getJSONObject(i).getString("publishedDate")));

                listTitles[i] = jsonString.getJSONObject(i).getString("title");
            }
            return new ArticleList(listTitles, articles);
        }
        catch (IOException | JSONException error) {
            error.printStackTrace();
        }
        return new ArticleList();
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