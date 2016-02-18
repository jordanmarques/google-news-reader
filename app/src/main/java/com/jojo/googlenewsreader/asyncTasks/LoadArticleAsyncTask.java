package com.jojo.googlenewsreader.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.activities.MainActivity;
import com.jojo.googlenewsreader.arrayAdapter.ArticleArrayAdapter;
import com.jojo.googlenewsreader.dataBase.DAO.ArticleDAO;
import com.jojo.googlenewsreader.pojo.Article;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadArticleAsyncTask extends AsyncTask<Void, Void, List<Article>> {

    public static final String DEFAULT_RESEARCH = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=''";

    private static final String API_URL = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=";

    private String query;
    private Context context;
    private ListView listView;

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, new String[]{"Chargement en cours ..."});
        listView.setAdapter(adapter);
    }

    @Override
    protected List<Article> doInBackground(Void... params) {

        List<Article> articles = new ArrayList<>();
        try {
            if(query.equals(DEFAULT_RESEARCH)){
                articles = searchFromQuery(DEFAULT_RESEARCH);
            } else {
                query = URLEncoder.encode(query, "UTF-8");
                articles = searchFromQuery(API_URL + query);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return articles;
    }

    @Override
    protected void onPostExecute(List<Article> result) {
        MainActivity.setArticleList(result);
        ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(context, R.layout.article_line, result);
        listView.setAdapter(arrayAdapter);
    }

    private List<Article> searchFromQuery(String query) {
        try {
            MainActivity.articleCounter = 0;
            JSONArray jsonString = getJsonFromServer(query);
            List<Article> articleList = new ArrayList<>();
            ArticleDAO  articleDAO = new ArticleDAO(context);

            for(int i = 0; i<jsonString.length(); i++) {

               Article article = new Article(jsonString.getJSONObject(i).getString("title"),
                        jsonString.getJSONObject(i).getString("content"),
                        jsonString.getJSONObject(i).getJSONObject("image").getString("url"),
                        jsonString.getJSONObject(i).getString("url"),
                        jsonString.getJSONObject(i).getString("publisher"),
                        jsonString.getJSONObject(i).getString("publishedDate"));

                if(!articleDAO.isArticleInDB(article)){
                    long articleId = articleDAO.insertArticle(article);
                    articleList.add(articleDAO.findById(articleId));
                    MainActivity.articleCounter =+ 1;
                } else {
                    Article byTitleStrict = articleDAO.findByTitleStrict(article);
                    if(byTitleStrict.getDeleted() == 0) {
                        articleList.add(byTitleStrict);
                    }
                }
            }

            populatePublishDateField(articleList);
            sortByPublishedDate(articleList);

            return articleList;

        } catch (IOException | JSONException error) {
            error.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<Article>();
    }

    private void sortByPublishedDate(List<Article> articleList) {
        Collections.sort(articleList, new Comparator<Article>() {
            public int compare(Article o1, Article o2) {
                return o2.getPublisheDate().compareTo(o1.getPublisheDate());
            }
        });
    }

    private void populatePublishDateField(List<Article> articleList) throws ParseException {
        DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
        for(Article loopArticle : articleList){
            loopArticle.setDate(loopArticle.getDate().replace(" -0800", ""));
            loopArticle.setPublisheDate(format.parse(loopArticle.getDate()));
        }
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