//Homework 4
//MainActivity.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.appstorelatest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void display(View v){

        Intent intent = new Intent(MainActivity.this, MediaListActivity.class);
        intent.putExtra(AppStoreConstants.TITLE, ((TextView) v).getText().toString());

        switch(((TextView) v).getText().toString()) {

            case AppStoreConstants.AUDIO_BOOKS_LABEL:
            intent.putExtra(AppStoreConstants.LINK, AppStoreConstants.AUDIO_BOOKS_LINK);
                break;

            case AppStoreConstants.BOOKS:
                intent.putExtra(AppStoreConstants.LINK, AppStoreConstants.BOOKS_LINK);
                break;

            case AppStoreConstants.IOS_APPS:
                intent.putExtra(AppStoreConstants.LINK, AppStoreConstants.IOS_APPS_LINK);
                break;

            case AppStoreConstants.ITUNES_U:
                intent.putExtra(AppStoreConstants.LINK, AppStoreConstants.ITUNES_U_LINK);
                break;

            case AppStoreConstants.MAC_APPS:
                intent.putExtra(AppStoreConstants.LINK, AppStoreConstants.MAC_APPS_LINK);
                break;

            case AppStoreConstants.MOVIES:
                intent.putExtra(AppStoreConstants.LINK, AppStoreConstants.MOVIES_LINK);
                break;

            case AppStoreConstants.PODCASTS:
                intent.putExtra(AppStoreConstants.LINK, AppStoreConstants.PODCATS_LINK);
                break;

            case AppStoreConstants.TV_SHOWS:
                intent.putExtra(AppStoreConstants.LINK, AppStoreConstants.TV_SHOWS_LINK);
                break;
        }

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
