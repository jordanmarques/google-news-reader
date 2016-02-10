package com.jojo.googlenewsreader.articles;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.pojo.Article;

import java.util.List;

public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    public ArticleArrayAdapter(Context context, int resource, List<Article> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View line = layoutInflater.inflate(R.layout.article_line, null);

        ImageView image = (ImageView) line.findViewById(R.id.imageViewArticle);
        TextView text = (TextView) line.findViewById(R.id.textViewArticle);

        Article article  = getItem(position);

        text.setText(Html.fromHtml(article.getTitle()));
        ImageDownloadAsyncTask imageDownloadAsyncTask = new ImageDownloadAsyncTask(image, article.getImageUrl());
        imageDownloadAsyncTask.execute();

        return line;
    }


}
