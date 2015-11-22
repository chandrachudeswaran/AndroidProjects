//Homework 4
//MediaListActivity.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.appstorelatest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;

public class MediaListActivity extends AppCompatActivity {

    String url;
    ProgressDialog dialog;
    String title;
    LinearLayout linearLayout;
    ArrayList<MediaList> list;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);

        linearLayout = (LinearLayout) findViewById(R.id.parentLinearLayout);
        title = getIntent().getExtras().getString(AppStoreConstants.TITLE);
        setTitle(title);

        url = getIntent().getExtras().getString(AppStoreConstants.LINK);

        long present = loadSavedPreferences();
        long diff = (System.currentTimeMillis()-present)/60000;

        if(diff>=2){
            if (isConnected()) {
                new DownloadURL().execute(url, title);
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection.Try again later", Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            display(list);
        }


    }

    private class DownloadURL extends AsyncTask<String, Void, ArrayList<MediaList>> {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MediaListActivity.this);
            dialog.setMessage("Loading" + " " + " " + title);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected ArrayList<MediaList> doInBackground(String... params) {
            try {
                BufferedReader reader = null;
                String line = "";
                StringBuilder sb = new StringBuilder();
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return Util.MediaListParser.parseMedia(sb.toString(), params[1]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MediaList> mediaLists) {
            dialog.dismiss();
            list = mediaLists;


            SharedPreferences.Editor editor;
            sharedPreferences = MediaListActivity.this.getSharedPreferences(title, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String jsonMedia = gson.toJson(list);
            editor.putString("LIST", jsonMedia);
            editor.putLong("TIME", System.currentTimeMillis());
            editor.commit();
            display(mediaLists);

        }
    }

    public void putSharedPreferences(){

        SharedPreferences.Editor editor;
        sharedPreferences = MediaListActivity.this.getSharedPreferences(title, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonMedia = gson.toJson(list);
        editor.putString("LIST", jsonMedia);
        editor.commit();
    }

    public void display(ArrayList<MediaList> mediaLists) {
        int i = 0;
        for (MediaList media : mediaLists) {

            LinearLayout layout = new LinearLayout(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(layoutParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setOnClickListener(click);
            layout.setOnLongClickListener(longClick);
            layout.setId(i);

            ImageView imageView = new ImageView(this);
            ViewGroup.LayoutParams layoutChild = new ViewGroup.LayoutParams(200, 200);
            imageView.setLayoutParams(layoutChild);
            Picasso.with(getApplicationContext()).load(media.getSmall_url()).into(imageView);

            TextView empty = new TextView(this);
            ViewGroup.LayoutParams layoutEmpty = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            empty.setLayoutParams(layoutEmpty);
            empty.setText("   ");

            TextView textView = new TextView(this);
            ViewGroup.LayoutParams layoutText = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutText);
            textView.setText(media.getTitle());
            textView.setTextAppearance(this, android.R.style.TextAppearance_Large);
            layout.addView(imageView);
            layout.addView(empty);
            layout.addView(textView);
            linearLayout.addView(layout);
            i++;

        }
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            Intent intent = new Intent(MediaListActivity.this, DetailedMediaActivity.class);
            intent.putExtra("Type", title);
            intent.putExtra("Title1", title + "" + " " + "Details");
            intent.putExtra("Media", list.get(i));
            startActivity(intent);
        }
    };


    View.OnLongClickListener longClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            int i = v.getId();
            linearLayout.removeViewAt(i);
            list.remove(i);
            updateViewId();
            putSharedPreferences();
            return true;
        }
    };


    @Override
    public void onBackPressed() {
        finish();
    }

    public void updateViewId() {
        int i = 0;
        for (MediaList temp : list) {
            linearLayout.getChildAt(i).setId(i);
            i++;
        }

    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }


    public long loadSavedPreferences() {
        long beginning = 0;
        sharedPreferences = getSharedPreferences(title, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("LIST")) {
            beginning = sharedPreferences.getLong("TIME", 0);
            String jsonMedia = sharedPreferences.getString("LIST", null);
            Gson gson = new Gson();
            MediaList[] media = gson.fromJson(jsonMedia, MediaList[].class);
            list = new ArrayList<MediaList>();
            for (MediaList t : media) {
                list.add(t);
            }
        }
        return beginning;
    }
}

