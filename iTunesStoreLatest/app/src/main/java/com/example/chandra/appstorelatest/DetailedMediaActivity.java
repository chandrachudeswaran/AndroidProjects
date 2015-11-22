//Homework 4
//DetailedMediaActivity.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.appstorelatest;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import android.graphics.Color;
import android.widget.Toast;

import org.w3c.dom.Text;

public class DetailedMediaActivity extends AppCompatActivity {

    TextView title;
    TextView releaseDate;
    ImageView image;
    MediaList media;
    LinearLayout layout;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_media);

        title = (TextView) findViewById(R.id.title);
        releaseDate = (TextView) findViewById(R.id.release);
        image = (ImageView) findViewById(R.id.image);
        layout = (LinearLayout) findViewById(R.id.parentLayout);

        setTitle(getIntent().getExtras().getString("Title1"));

        media = getIntent().getExtras().getParcelable("Media");
        type = getIntent().getExtras().getString("Type");

        if(isConnected()) {
            display();
        }
        else{
            Toast.makeText(getApplicationContext(), "No Internet Connection.Try again later", Toast.LENGTH_SHORT).show();
            return;
        }


    }


    public void display() {

        title.setText(media.getTitle());
        Picasso.with(getApplicationContext()).load(media.getLarge_url()).into(image);

        layout.addView(createTextView("Artist" + " " + ":" + " " + media.getArtist(), true));
        TextView category = createTextView("Category" + " " + ":" + " " + media.getCategory(), true);
        layout.addView(category);
        releaseDate.setText(media.getReleaseDate());

        if (type.equals(AppStoreConstants.AUDIO_BOOKS_LABEL)) {
            layout.addView(createTextView("Duration" + " " + ":" + " " + media.getDuration(), true));
        } else {
            if (type.equals(AppStoreConstants.IOS_APPS) || (type.equals(AppStoreConstants.MOVIES))) {
                layout.addView(createTextView("Price" + " " + ":" + " " + "$" + " " + media.getPrice(), true));
            } else {
                if(type.equals(AppStoreConstants.PODCASTS)){
                    layout.removeView(category);
                }
                layout.addView(createTextView("Summary" + " " + ":" + " " + media.getSummary(), true));
                layout.addView(createTextView("Price" + " " + ":" + " " + "$" + " " + media.getPrice(), true));
            }
        }


        layout.addView(createTextView("App Link in Store:", true));
        TextView text = createTextView(" " + media.getLink(), false);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedMediaActivity.this,PreviewActivity.class);
                intent.putExtra("url",media.getLink());
                startActivity(intent);
            }
        });
        layout.addView(text);
        layout.addView(createTextView(" ", true));
        if(type.equals(AppStoreConstants.MOVIES)|| type.equals(AppStoreConstants.ITUNES_U)|| type.equals(AppStoreConstants.PODCASTS)){
            releaseDate.setText("");

        }

    }


    public TextView createTextView(String text, boolean cond) {

        TextView view = new TextView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        if (cond) {
            view.setTextAppearance(this, android.R.style.TextAppearance_Large);
        } else {
            view.setTextAppearance(this, android.R.style.TextAppearance_Small);
            view.setTextColor(getResources().getColor(R.color.md_blue_800));
        }
        view.setText(text);
        return view;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
}
