package com.jojo.googlenewsreader.arrayAdapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.activities.MainActivity;
import com.jojo.googlenewsreader.asyncTasks.ImageDownloadAsyncTask;
import com.jojo.googlenewsreader.pojo.Article;
import com.jojo.googlenewsreader.pojo.Tag;
import com.jojo.googlenewsreader.utils.NetworkUtil;

import java.util.List;

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    private Context context;

    public ArticleArrayAdapter(Context context, int resource, List<Article> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View line = layoutInflater.inflate(R.layout.article_line, null);

        TextView text = (TextView) line.findViewById(R.id.textViewArticle);
        ImageView image = (ImageView) line.findViewById(R.id.imageViewArticle);
        TextView tags = (TextView) line.findViewById(R.id.tags);

        Article article  = getItem(position);

        text.setText(Html.fromHtml(article.getTitle()));
        tags.setText(formatTagsForDisplay(article));


         if( null != article.getBitmapImage()) {
             image.setImageBitmap(article.getBitmapImage());
         }else if(!NetworkUtil.getConnectivityStatusBoolean(context)){
             image.setBackground(ContextCompat.getDrawable(context, R.drawable.no_image));
         } else {
            ImageDownloadAsyncTask imageDownloadAsyncTask = new ImageDownloadAsyncTask(getContext(), image, article);
            imageDownloadAsyncTask.execute();
        }



        return line;
    }

    public String formatTagsForDisplay(Article article){
        String formatedTags = "";

        for(Tag tag : article.getTagList()){
            formatedTags = formatedTags + tag.getLabel() + " ";
        }

        return formatedTags;
    }


}
