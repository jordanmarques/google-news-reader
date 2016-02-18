package com.jojo.googlenewsreader.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.PopupMenu;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.arrayAdapter.ArticleArrayAdapter;
import com.jojo.googlenewsreader.asyncTasks.LoadArticleAsyncTask;
import com.jojo.googlenewsreader.dataBase.DAO.ArticleDAO;
import com.jojo.googlenewsreader.dataBase.DAO.ArticleTagDAO;
import com.jojo.googlenewsreader.dataBase.DAO.TagDAO;
import com.jojo.googlenewsreader.pojo.Article;
import com.jojo.googlenewsreader.pojo.Tag;
import com.jojo.googlenewsreader.utils.NetworkUtil;

import java.util.List;


public class MainActivity extends ParentActivity {

    public static final String INIT_SEARCH = "init_search";

    private ListView listView;
    private ArticleDAO articleDAO;
    private ArticleTagDAO articleTagDAO;
    private static TextView label;

    public static List<Article> articleList;
    public static String currentQuery = "";
    public static int articleCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        articleDAO = new ArticleDAO(this);
        articleTagDAO = new ArticleTagDAO(this);

        listView = (ListView) findViewById(R.id.listView);
        registerForContextMenu(listView);

        SearchView searchView = (SearchView)findViewById(R.id.searchView);
        Button tagButton = (Button) findViewById(R.id.button);
        Button refreshButton = (Button) findViewById(R.id.button2);
        Button lastNewsButton = (Button) findViewById(R.id.button3);
        label = (TextView) findViewById(R.id.networkLabel);


        search(INIT_SEARCH);
        launchAutoUpdate();

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
                if(NetworkUtil.getConnectivityStatusBoolean(MainActivity.this)){
                    Toast.makeText(getApplicationContext(), "Chargement des dernier articles...", Toast.LENGTH_SHORT).show();
                }
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

                View child = listView.getChildAt(position - listView.getFirstVisiblePosition());
                if(NetworkUtil.getConnectivityStatusBoolean(MainActivity.this)) {
                    ImageView imageView = (ImageView) child.findViewById(R.id.imageViewArticle);
                    imageView.buildDrawingCache();
                    ArticleDetail.setImage(imageView.getDrawingCache());
                }


                Intent intent = new Intent(MainActivity.this, ArticleDetail.class);
                intent.putExtra("articleForDetailActivity", articleList.get(position));

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
                    deleteArticle(article);
                    articleList.remove(article);

                    ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(MainActivity.this, R.layout.article_line, articleList);
                    getListView().setAdapter(arrayAdapter);

                    Toast.makeText(MainActivity.this, "Article supprimé ", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            menu.add(Menu.NONE, 3, 3, "Ajouter Tag").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final int position = acmi.position;
                    PopupMenu popup = new PopupMenu(MainActivity.this, getViewByPosition(position, listView));

                    final TagDAO tagDAO = new TagDAO(MainActivity.this);
                    List<Tag> allTags = tagDAO.findAllTags();
                    for (Tag tag : allTags) {
                        popup.getMenu().add(1, 1, 1, tag.getLabel());
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Tag tag = tagDAO.findTagByTitle(String.valueOf(item.getTitle()));
                            addTagToArticle(tag, article, listView, position);
                            Toast.makeText(MainActivity.this, "Article lié au Tag: " + tag.getLabel(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                    popup.show();
                    return false;
                }
            });

            menu.add(Menu.NONE, 4, 4, "Supprimer Tag").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final int position = acmi.position;
                    PopupMenu popup = new PopupMenu(MainActivity.this, getViewByPosition(position, listView));

                    final TagDAO tagDAO = new TagDAO(MainActivity.this);

                    List<Tag> articleTags = article.getTagList();
                    for (Tag tag : articleTags) {
                        popup.getMenu().add(1, 1, 1, tag.getLabel());
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Tag tag = tagDAO.findTagByTitle(String.valueOf(item.getTitle()));
                            removeTagToArticle(tag, article, listView, position);
                            Toast.makeText(MainActivity.this, "Tag " + tag.getLabel() +" supprimé de l'article", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                    popup.show();
                    return false;
                }
            });
        }

    }

    public static void onNetworkChange(Boolean state){
        if(null != label){
            if(state){
                label.setVisibility(View.INVISIBLE);
            } else {
                label.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addTagToArticle(Tag tag, Article article, ListView listView, int position) {
        articleTagDAO.insertArticleTagLink(article, tag);
        View child = listView.getChildAt(position);
        TextView textView= (TextView) child.findViewById(R.id.tags);

        textView.setText(formatTagToAdd(tag.getLabel(), String.valueOf(textView.getText())));
        articleList.get(position).getTagList().add(tag);
    }

    private void removeTagToArticle(Tag tag, Article article, ListView listView, int position) {
        articleTagDAO.deleteArticleTagLink(article, tag);
        View child = listView.getChildAt(position);
        TextView textView= (TextView) child.findViewById(R.id.tags);

        textView.setText(formatTagToRemove(tag.getLabel(), String.valueOf(textView.getText())));
        articleList.get(position).getTagList().add(tag);
    }

    private String formatTagToAdd(String tagToAdd, String textViewValue) {
        return textViewValue + " " + tagToAdd;
    }

    private String formatTagToRemove(String tagToAdd, String textViewValue) {
        return textViewValue.replace(tagToAdd, "");
    }

    private void search(String query) {

        if(query.charAt(0) == "#".charAt(0)) {
            tagSearch(query);
        }else if(NetworkUtil.getConnectivityStatusBoolean(MainActivity.this)){
            webSearch(query);
        } else {
            localSearch(query);
        }
    }

    private void tagSearch(String query) {
        articleList = articleDAO.findByTag(query.substring(1));
        ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(this, R.layout.article_line, articleList);
        listView.setAdapter(arrayAdapter);
        setCurrentQuery(INIT_SEARCH);
    }

    private void localSearch(String query) {
        if(query.equals(INIT_SEARCH)){
            articleList = articleDAO.findAllArticles();
            ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(this, R.layout.article_line, articleList);
            listView.setAdapter(arrayAdapter);
            setCurrentQuery(INIT_SEARCH);
        } else {
            articleList = articleDAO.findByTitle(query);
            ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(this, R.layout.article_line, articleList);
            listView.setAdapter(arrayAdapter);
            setCurrentQuery(query);
        }
    }

    private void webSearch(String query) {
        if(query.equals(INIT_SEARCH)){
            LoadArticleAsyncTask task = new LoadArticleAsyncTask(MainActivity.this , listView, LoadArticleAsyncTask.DEFAULT_RESEARCH);
            task.execute();
            setCurrentQuery(INIT_SEARCH);
        } else {
            LoadArticleAsyncTask task = new LoadArticleAsyncTask(MainActivity.this , listView, query);
            task.execute();
            setCurrentQuery(query);
        }
    }

    public void deleteArticle(Article article){
        articleDAO.deleteArticle(article);
        articleTagDAO.deleteArticleTagLink(article);
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

    public void notification(String content){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Google News Reader")
                .setAutoCancel(true)
                .setContentText(content);

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(this);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    public void launchAutoUpdate(){
        final Handler h = new Handler();
        final int delay = 10*60*1*1000; //minutes

        h.postDelayed(new Runnable() {
            public void run() {
                if (NetworkUtil.getConnectivityStatusBoolean(MainActivity.this)) {
                    search(INIT_SEARCH);
                    switch (articleCounter) {
                        case 0:
                            notification("Aucun nouvel article chargé");
                            break;
                        case 1:
                            notification("1 nouvel article chargé");
                            break;
                        default:
                            notification(articleCounter + " nouveaux articles chargés");
                            break;
                    }
                } else {
                    notification("Chargement Impossible: Pas de Réseau");
                }
                h.postDelayed(this, delay);
            }
        }, delay);
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
