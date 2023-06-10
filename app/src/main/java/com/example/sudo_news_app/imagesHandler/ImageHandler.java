package com.example.sudo_news_app.imagesHandler;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.sudo_news_app.MainActivity;

import java.io.InputStream;

public class ImageHandler extends AsyncTask<String, Void, Bitmap> {

    @SuppressLint("StaticFieldLeak")
    ImageView imageView;
    @SuppressLint("StaticFieldLeak")
    MainActivity mainActivity;
    InputStream inputStream;

    public ImageHandler(ImageView imageView, MainActivity mainActivity) {
        this.imageView = imageView;
        this.mainActivity = mainActivity;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmapIcon;
        try {
            inputStream = new java.net.URL(strings[0]).openStream();
            bitmapIcon = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            bitmapIcon = null;
            Log.e("ImageHandler", "Issue with Image Error: doInBackground");
            e.printStackTrace();
        }
        return bitmapIcon;
    }

    protected void onPostExecute(Bitmap onPostResult) {
        if (onPostResult == null) {
            imageView.setImageResource(mainActivity.getResources().getIdentifier("brokenimage", "drawable", mainActivity.getPackageName()));
        } else {
            imageView.setImageBitmap(onPostResult);
        }
    }
}
