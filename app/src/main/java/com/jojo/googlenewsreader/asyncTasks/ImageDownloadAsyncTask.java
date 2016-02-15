package com.jojo.googlenewsreader.asyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.jojo.googlenewsreader.pojo.Article;

import java.net.URL;

public class ImageDownloadAsyncTask extends AsyncTask<Void, Bitmap, Bitmap> {
    private ImageView imageView;
    private Article article;

    public ImageDownloadAsyncTask(ImageView imageView, Article article) {
        this.imageView = imageView;
        this.article = article;
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
        imageView.setImageBitmap(bitmap);
    }
}
