package com.jojo.googlenewsreader.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.arrayAdapter.ArticleArrayAdapter;
import com.jojo.googlenewsreader.arrayAdapter.TagArrayAdapter;
import com.jojo.googlenewsreader.dataBase.DAO.TagDAO;
import com.jojo.googlenewsreader.pojo.Tag;

import java.util.List;

public class TagManager extends AppCompatActivity {

    private TagDAO tagDAO;
    private static List<Tag> allTags;
    private static TagArrayAdapter tagArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_manager);

        final EditText editText = (EditText) findViewById(R.id.editText);
        Button createButton = (Button) findViewById(R.id.button7);
        ListView listView = (ListView) findViewById(R.id.listView2);
        registerForContextMenu(listView);

        tagDAO = new TagDAO(this);
        allTags = tagDAO.findAllTags();

        tagArrayAdapter = new TagArrayAdapter(this, R.id.listView2, allTags);
        tagArrayAdapter.setNotifyOnChange(true);
        listView.setAdapter(tagArrayAdapter);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tagId = tagDAO.insertTag(new Tag(String.valueOf(editText.getText())));

                allTags.add(tagDAO.findById(tagId));
                getTagArrayAdapter().notifyDataSetChanged();

                editText.setText("");
                hideSoftKeyboard(TagManager.this);
            }
        });




    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listView2) {

            ListView listView = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            final Tag tag = (Tag) listView.getItemAtPosition(acmi.position);

            menu.setHeaderTitle("Options");
            menu.add(Menu.NONE,1 ,1, "Supprimer").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    tagDAO.deleteTag(tag);
                    getAllTags().remove(tag);
                    getTagArrayAdapter().notifyDataSetChanged();
                    return false;
                }
            });
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static TagArrayAdapter getTagArrayAdapter() {
        return tagArrayAdapter;
    }
    public static void setTagArrayAdapter(TagArrayAdapter tagArrayAdapter) {
        TagManager.tagArrayAdapter = tagArrayAdapter;
    }

    public static List<Tag> getAllTags() {
        return allTags;
    }
    public static void setAllTags(List<Tag> allTags) {
        TagManager.allTags = allTags;
    }
}
