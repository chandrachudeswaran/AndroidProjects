//Homework 4
//PreviewActivity.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.appstorelatest;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.content.Intent;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        if(isConnected()) {
            myWebView.setWebViewClient(new MyWebViewClient());
            Toast.makeText(getApplicationContext(),"Loading..",Toast.LENGTH_SHORT).show();
            myWebView.loadUrl(getIntent().getExtras().getString("url"));
        }
        else{
            Toast.makeText(getApplicationContext(),"No Internet Connection.Try again later",Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return false;
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


    @Override
    public void onBackPressed() {
        finish();
    }
}