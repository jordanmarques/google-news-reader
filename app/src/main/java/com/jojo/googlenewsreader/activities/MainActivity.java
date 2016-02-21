package com.jojo.googlenewsreader.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.arrayAdapter.ArticleArrayAdapter;
import com.jojo.googlenewsreader.asyncTasks.LoadArticleAsyncTask;
import com.jojo.googlenewsreader.brodcastReceiver.AlarmBroadcastReceiver;
import com.jojo.googlenewsreader.dataBase.DAO.ArticleDAO;
import com.jojo.googlenewsreader.dataBase.DAO.ArticleTagDAO;
import com.jojo.googlenewsreader.dataBase.DAO.TagDAO;
import com.jojo.googlenewsreader.pojo.Article;
import com.jojo.googlenewsreader.pojo.ArticleForSerialization;
import com.jojo.googlenewsreader.pojo.Tag;
import com.jojo.googlenewsreader.utils.NetworkUtil;
import com.jojo.googlenewsreader.utils.Utils;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ParentActivity {

    public static final String INIT_SEARCH = "init_search";

    private static ArticleDAO articleDAO;
    private ArticleTagDAO articleTagDAO;
    private Runnable runnable;
    private Handler handler;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private static TextView label;

    public static List<Article> articleList;
    public static ListView listView;
    public static Context context;
    public static int articleCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_home);
        articleDAO = new ArticleDAO(this);
        articleTagDAO = new ArticleTagDAO(this);

        listView = (ListView) findViewById(R.id.article_listView);
        registerForContextMenu(listView);

        SearchView searchView = (SearchView)findViewById(R.id.searchView);
        Button tagButton = (Button) findViewById(R.id.tag_button);
        Button lastNewsButton = (Button) findViewById(R.id.refresh_button);
        label = (TextView) findViewById(R.id.networkLabel);


        final Button optionMenu = new Button(this);
        optionMenu.setBackground(ContextCompat.getDrawable(this, R.drawable.menu_action));

        RelativeLayout titleBar = (RelativeLayout) findViewById(R.id.relativeLayoutTitleBar);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(110,130);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        titleBar.addView(optionMenu, layoutParams);

        search(INIT_SEARCH);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);


        optionMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                SpannableString title = new SpannableString("Mise à Jour : ");
                title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), 0, title.length(), 0);

                PopupMenu popupOption = new PopupMenu(MainActivity.this, optionMenu);

                popupOption.getMenu().add(1, 1, 1, "").setTitle(title);

                popupOption.getMenu().add(1, 1, 1, "1 minute").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),60*1000, pendingIntent);
                        Toast.makeText(MainActivity.this, "Mise à jour automatique toutes les minutes", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                popupOption.getMenu().add(1, 1, 1, "15 minutes").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 15 * 60 * 1000, pendingIntent);
                        Toast.makeText(MainActivity.this, "Mise à jour automatique toutes les 15 minutes", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                popupOption.getMenu().add(1, 1, 1, "30 minutes").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30 * 60 * 1000, pendingIntent);
                        Toast.makeText(MainActivity.this, "Mise à jour automatique toutes les 30 minutes", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                popupOption.getMenu().add(1, 1, 1, "60 minutes").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60 * 60 * 1000, pendingIntent);
                        Toast.makeText(MainActivity.this, "Mise à jour automatique toutes les 60 minutes", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                popupOption.getMenu().add(1, 1, 1, "Désactiver").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        alarmManager.cancel(pendingIntent);
                        Toast.makeText(MainActivity.this, "Mise à jour automatique désactivé", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                popupOption.show();


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
                intent.putExtra("articleForDetailActivity", new ArticleForSerialization(articleList.get(position)));

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
    protected void onResume() {
        super.onResume();
        MainActivity.articleCounter = 0;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.article_listView) {

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

    public static void onNetworkChange(Boolean availableNetwork){
        if(null != label){
            if(availableNetwork){
                label.setVisibility(View.INVISIBLE);
            } else {
                label.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addTagToArticle(Tag tag, Article article, ListView listView, int position) {
        articleTagDAO.insertArticleTagLink(article, tag);
        View child = listView.getChildAt(position - listView.getFirstVisiblePosition());
        TextView textView= (TextView) child.findViewById(R.id.tags);

        textView.setText(formatTagToAdd(tag.getLabel(), String.valueOf(textView.getText())));
        articleList.get(position).getTagList().add(tag);
    }

    private void removeTagToArticle(Tag tag, Article article, ListView listView, int position) {
        articleTagDAO.deleteArticleTagLink(article, tag);
        View child = listView.getChildAt(position - listView.getFirstVisiblePosition());
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

    public void search(String query) {

        if(query.charAt(0) == "#".charAt(0)) {
            tagSearch(query);
        }else if(NetworkUtil.getConnectivityStatusBoolean(context)){
            webSearch(query);
        } else {
            localSearch(query);
        }
    }

    private void tagSearch(String query) {
        articleList = articleDAO.findByTag(query.substring(1));
        try {
            Utils.populatePublishDateField(articleList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Utils.sortByPublishedDate(articleList);
        ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(context, R.layout.article_line, articleList);
        listView.setAdapter(arrayAdapter);
    }

    private void localSearch(String query) {
        if(query.equals(INIT_SEARCH)){
            articleList = articleDAO.findAllArticles();
            try {
                Utils.populatePublishDateField(articleList);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Utils.sortByPublishedDate(articleList);
            ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(context, R.layout.article_line, articleList);
            listView.setAdapter(arrayAdapter);
        } else {
            articleList = articleDAO.findByTitle(query);
            try {
                Utils.populatePublishDateField(articleList);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Utils.sortByPublishedDate(articleList);
            ArticleArrayAdapter arrayAdapter = new ArticleArrayAdapter(context, R.layout.article_line, articleList);
            listView.setAdapter(arrayAdapter);
        }
    }

    private void webSearch(String query) {
        if(query.equals(INIT_SEARCH)){
            LoadArticleAsyncTask task = new LoadArticleAsyncTask(context , listView, LoadArticleAsyncTask.DEFAULT_SEARCH);
            task.execute();
        } else {
            LoadArticleAsyncTask task = new LoadArticleAsyncTask(context , listView, query);
            task.execute();
        }
    }

    public static void waitingSearch(Context context){
        LoadArticleAsyncTask task = new LoadArticleAsyncTask(context , listView, LoadArticleAsyncTask.DEFAULT_SEARCH);
        try {
            task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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

    public static void setArticleList(List articleList) {
        MainActivity.articleList = articleList;
    }

    public ListView getListView() {
        return listView;
    }
    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
