//HW 5
//MainActivity.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.tedradiohourpodcast;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    ProgressDialog dialog;
    ArrayList<Item> itemsList;
    RecyclerView recyclerView;
    boolean condition;
    public static final String REQUEST_INTENT = "Item";

    MainActivityUtility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        final LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        condition = true;

        new DownloadItems().execute("http://www.npr.org/rss/podcast.php?id=510298");
    }


    private class DownloadItems extends AsyncTask<String, Void, ArrayList<Item>> {

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading Episodes");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected ArrayList<Item> doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                int status = con.getResponseCode();

                if (status == HttpURLConnection.HTTP_OK) {
                    InputStream in = con.getInputStream();
                    return ParserUtil.XMLParser.parseItems(in);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            super.onPostExecute(items);
            dialog.dismiss();
            itemsList = items;
            createView(condition);

        }
    }

    public void createView(boolean condition) {
        if (utility == null) {
            utility = new MainActivityUtility(this);
        } else {
            utility.reset();
        }

        if (condition) {

            recyclerView = (RecyclerView) findViewById(R.id.recycleView);

            final LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(itemsList, MainActivity.this, utility);
            recyclerView.setAdapter(adapter);

        } else {

            recyclerView = (RecyclerView) findViewById(R.id.recycleView);
            final GridLayoutManager grid = new GridLayoutManager(MainActivity.this, 2);
            grid.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(grid);
            GridAdapter adapter = new GridAdapter(itemsList, MainActivity.this, utility);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.switchLayouts:
                condition = !condition;
                createView(condition);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPlay(View v) {
        utility.playMedia(v);
    }

    public void onPause(View v) {
        utility.pauseMedia(v);
    }

    @Override
    public void onBackPressed() {
        utility.back();
        finish();
    }
}
