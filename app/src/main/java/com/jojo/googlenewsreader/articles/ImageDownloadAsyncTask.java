package com.jojo.googlenewsreader.articles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;

public class ImageDownloadAsyncTask extends AsyncTask<Void, Bitmap, Bitmap> {
    private ImageView imageView;
    private String url;

    public ImageDownloadAsyncTask(ImageView imageView, String url) {
        this.imageView = imageView;
        this.url = url;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL newurl = new URL(url);
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
