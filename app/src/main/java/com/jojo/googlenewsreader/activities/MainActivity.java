package com.jojo.googlenewsreader.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.arrayAdapter.ArticleArrayAdapter;
import com.jojo.googlenewsreader.asyncTasks.LoadArticleAsyncTask;
import com.jojo.googlenewsreader.dataBase.DAO.ArticleDAO;
import com.jojo.googlenewsreader.dataBase.DAO.TagDAO;
import com.jojo.googlenewsreader.pojo.Article;
import com.jojo.googlenewsreader.pojo.Tag;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String INIT_SEARCH = "init_search";

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
        articleDAO.findAllArticles();
        articleDAO.findById(3);

        listView = (ListView) findViewById(R.id.listView);
        registerForContextMenu(listView);

        SearchView searchView = (SearchView)findViewById(R.id.searchView);
        Button tagButton = (Button) findViewById(R.id.button);
        Button refreshButton = (Button) findViewById(R.id.button2);
        Button lastNewsButton = (Button) findViewById(R.id.button3);

        search(INIT_SEARCH);

        refreshButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentQuery = MainActivity.getCurrentQuery();
                if (!currentQuery.trim().equals("")) {
                    search(currentQuery);
                }
            }
        });

        lastNewsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                search(INIT_SEARCH);
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
                Log.d("MainActivity", articleList.get(itemPosition).toString());
                intent.putExtra("articleForDetailActivity", articleList.get(itemPosition));
                startActivityForResult(intent, 0);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
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

            final ListView listView = (ListView) v;
            final AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            final Article article = (Article) listView.getItemAtPosition(acmi.position);

            menu.setHeaderTitle("Options");

            menu.add(Menu.NONE,2 ,2, "Supprimer").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    articleDAO.deleteArticle(article);
                    articleList.remove(article);

                    ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(MainActivity.this, R.layout.article_line, articleList);
                    getListView().setAdapter(arrayAdapter);

                    return false;
                }
            });

            menu.add(Menu.NONE, 3, 3, "Ajouter Tag").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    //findViewById(R.id.listView)
                    PopupMenu popup = new PopupMenu(MainActivity.this, getViewByPosition(acmi.position, listView));

                    //empecher fermer context menungui
                    //http://stackoverflow.com/questions/13784088/setting-popupmenu-menu-items-programmatically
                    //http://stackoverflow.com/questions/21329132/android-custom-dropdown-popup-menu
                    //http://stackoverflow.com/questions/8002756/sqlite-composite-key-2-foreign-keys-link-table
                    popup.getMenu().add(Menu.NONE, 1, 1, "TOTO");

                    TagDAO tagDAO = new TagDAO(MainActivity.this);
                    List<Tag> allTags = tagDAO.findAllTags();
                    for (Tag tag : allTags) {
                        popup.getMenu().add(1, 1, 1, tag.getLabel());
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            //TODO: link between article and tag
                            Toast.makeText(MainActivity.this, item.getTitle() + " " + article.getTitle(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                    popup.show();
                    return false;
                }
            });
        }

    }

    private void search(String query) {

        if(isNetworkAvailable()){
            if(query.equals(INIT_SEARCH)){
                LoadArticleAsyncTask task = new LoadArticleAsyncTask(MainActivity.this , listView, LoadArticleAsyncTask.DEFAULT_RESEARCH);
                task.execute();
                setCurrentQuery(INIT_SEARCH);
            } else {
                LoadArticleAsyncTask task = new LoadArticleAsyncTask(MainActivity.this , listView, query);
                task.execute();
                setCurrentQuery(query);
            }
        } else {
            if(query.equals(INIT_SEARCH)){
                List<Article> allArticles = articleDAO.findAllArticles();
                ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(this, R.layout.article_line, allArticles);
                listView.setAdapter(arrayAdapter);
                setCurrentQuery(INIT_SEARCH);
            } else {
                List<Article> result = articleDAO.findByTitle(query);
                MainActivity.setArticleList(result);
                ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(this, R.layout.article_line, result);
                listView.setAdapter(arrayAdapter);
                setCurrentQuery(query);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
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

    public ListView getListView() {
        return listView;
    }
    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
