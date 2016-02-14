package com.jojo.googlenewsreader.arrayAdapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.asyncTasks.ImageDownloadAsyncTask;
import com.jojo.googlenewsreader.pojo.Article;
import com.jojo.googlenewsreader.pojo.Tag;

import java.util.List;

public class TagArrayAdapter extends ArrayAdapter<Tag> {


    public TagArrayAdapter(Context context, int resource, List<Tag> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View line = layoutInflater.inflate(R.layout.tag_line, null);

        TextView text = (TextView) line.findViewById(R.id.textViewTag);

        Tag tag  = getItem(position);

        text.setText(Html.fromHtml(tag.getLabel()));

        return line;
    }


}
