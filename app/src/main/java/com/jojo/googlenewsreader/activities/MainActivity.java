package com.jojo.googlenewsreader.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.articles.ArticleArrayAdapter;
import com.jojo.googlenewsreader.asyncTasks.LoadArticleAsyncTask;
import com.jojo.googlenewsreader.dataBase.DAO.ArticleDAO;
import com.jojo.googlenewsreader.pojo.Article;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArticleDAO articleDAO;

    public static List<Article> articleList;
    public static String currentQuery = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        articleDAO = new ArticleDAO(this);


//        Test DAO
//        articleDAO.insertArticle(new Article("Title", "Content", "imageUrl", "jojo magazine", "04 avril 2123", "url"));
        articleDAO.findAllArticles();

        listView = (ListView) findViewById(R.id.listView);
        SearchView searchView = (SearchView)findViewById(R.id.searchView);
        Button tagButton = (Button) findViewById(R.id.button);
        Button refreshButton = (Button) findViewById(R.id.button2);
        registerForContextMenu(listView);

        refreshButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentQuery = MainActivity.getCurrentQuery();
                if(!currentQuery.trim().equals("")){
                    search(currentQuery);
                }
            }
        });

        tagButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TagManager.class);
                startActivityForResult(intent, 0);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;

                Intent intent = new Intent(MainActivity.this, ArticleDetail.class);
                intent.putExtra("articleForDetailActivity", articleList.get(itemPosition));
                startActivityForResult(intent, 0);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                search(query);
                MainActivity.setCurrentQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("Options");
            menu.add(Menu.NONE,2 ,2, "Supprimer");
            menu.add(Menu.NONE,3 ,3, "Ajouter Tag");
        }
    }

    private void search(String query) {

        //si pas de reseau on fait une recherche dans la db et on donne le resultat a article array adapter
        if(isNetworkAvailable()){
            LoadArticleAsyncTask task = new LoadArticleAsyncTask(MainActivity.this , listView, query);
            task.execute();
        } else {
            List<Article> result = articleDAO.findByTitle(query);
            MainActivity.setArticleList(result);
            ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(this, R.layout.article_line, result);
            listView.setAdapter(arrayAdapter);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void setArticleList(List articleList) {
        MainActivity.articleList = articleList;
    }

    public static void setCurrentQuery(String currentQuery){
        MainActivity.currentQuery = currentQuery;
    }
    public static String getCurrentQuery(){
        return MainActivity.currentQuery;
    }
}
