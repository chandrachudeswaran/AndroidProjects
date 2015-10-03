//Homework 3
//LoadImage.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.triviaquiz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by chandra on 9/26/2015.
 */
public class LoadImage extends AsyncTask<RequestParams, Void, Bitmap> {
    ImageLoading imageLoading;

    public LoadImage(ImageLoading imageLoading) {
        this.imageLoading = imageLoading;
    }

    @Override
    protected Bitmap doInBackground(RequestParams... params) {
        InputStream in = null;

        try {
            HttpURLConnection con = params[0].getConnection();
            in = con.getInputStream();
            Bitmap image = BitmapFactory.decodeStream(in);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageLoading.sendImage(bitmap);
    }

    public interface ImageLoading {

        public void sendImage(Bitmap image);

    }
}
