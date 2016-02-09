package com.jojo.googlenewsreader.articles;

import android.os.AsyncTask;
import android.util.Log;

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

public class JSONAsyncTask extends AsyncTask<Void, Void, Void> {
    private String APIUrl = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0";
    private String query;


    @Override
    protected Void doInBackground(Void... params) {
        query = "barack%20obama";
        try {
            JSONArray jsonString = getJsonFromServer(APIUrl+"&q=barack%20obama");
            String[] listTitles = new String[jsonString.length()];
            List<Article> articles = new ArrayList<>();
            for(int i = 0; i<jsonString.length(); i++) {
                Article temporyArticle = new Article();
                articles.add(new Article(jsonString.getJSONObject(i).getString("title"),
                        jsonString.getJSONObject(i).getString("content"),
                        jsonString.getJSONObject(i).getString("image")));

                listTitles[i] = jsonString.getJSONObject(i).getString("title");
            }
            MainActivity.setListArticles(new ListArticles(listTitles, articles));
        }
        catch (IOException | JSONException error) {
            error.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

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