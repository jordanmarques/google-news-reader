package com.jojo.googlenewsreader.articles;

import android.os.AsyncTask;

import com.jojo.googlenewsreader.activities.Home;
import com.jojo.googlenewsreader.articles.ListArticles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Arnaud Flaesch on 31/01/2016.
 */
public class JSONAsyncTask extends AsyncTask<Void, Void, Void> {
    private String APIUrl = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0";


    @Override
    protected Void doInBackground(Void... params) {
        try {
            JSONArray jsonString = getJsonFromServer(APIUrl+"&q=barack%20obama");
            String[] listTitles = new String[jsonString.length()];
            for(int i = 0; i<jsonString.length(); i++) {
                listTitles[i] = jsonString.getJSONObject(i).getString("title");
            }
            Home.listArticles = new ListArticles(listTitles, jsonString);
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