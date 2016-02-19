package com.jojo.googlenewsreader.asyncTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.jojo.googlenewsreader.R;
import com.jojo.googlenewsreader.activities.MainActivity;
import com.jojo.googlenewsreader.pojo.Article;
import com.jojo.googlenewsreader.utils.NetworkUtil;

import java.net.URL;

public class ImageDownloadAsyncTask extends AsyncTask<Void, Bitmap, Bitmap> {
    private ImageView imageView;
    private Article article;
    private Context context;

    public ImageDownloadAsyncTask(Context context, ImageView imageView, Article article) {
        this.imageView = imageView;
        this.article = article;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            if(article.getImageUrl().isEmpty() || null == article.getImageUrl()){
                return null;
            }
            URL newurl = new URL(article.getImageUrl());
            return BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(null == bitmap){
            imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.no_image));
        } else {
            imageView.setImageBitmap(bitmap);
            imageView.setBackgroundColor(Color.argb(0, 0, 0, 0));
            article.setBitmapImage(bitmap);
        }

    }
}
