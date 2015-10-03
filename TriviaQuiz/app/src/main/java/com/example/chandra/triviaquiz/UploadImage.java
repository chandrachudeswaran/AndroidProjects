//Homework 3
//UploadImage.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.triviaquiz;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;

import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by chandra on 9/24/2015.
 */
public class UploadImage extends AsyncTask<String, Void, String> {

    Upload upload;

    public UploadImage(Upload upload) {
        this.upload = upload;
    }

    @Override
    protected String doInBackground(String... params) {


        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost postMethod = new HttpPost("http://dev.theappsdr.com/apis/trivia_fall15/uploadPhoto.php");
            File file = new File(params[0]);

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody contentFile = new FileBody(file);

            entity.addPart("uploaded_file", contentFile);

            postMethod.setEntity(entity);
            client.execute(postMethod);

            HttpResponse response = client.execute(postMethod);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();

            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            Log.d("url", s.toString());
            return s.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        upload.sendURL(s);
    }

    interface Upload {
        void sendURL(String url);
    }
}
