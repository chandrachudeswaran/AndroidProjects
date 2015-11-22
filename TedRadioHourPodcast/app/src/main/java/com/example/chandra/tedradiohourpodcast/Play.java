//HW 5
//Play.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.tedradiohourpodcast;

import android.app.ProgressDialog;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Play extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    ExecutorService threadPool;
    Item item;
    TextView title;
    ImageView image;
    TextView description;
    TextView date;
    TextView duration;
     MediaPlayer mediaPlayer;
    LinearLayout linear;
    boolean isPlaying;
    ImageView play;
    ImageView pause;
    Handler handler;
    int time;
    ProgressBar progressBar;
    ProgressDialog dialog;

    HandlerSync s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        threadPool = Executors.newFixedThreadPool(1);
        linear = (LinearLayout) findViewById(R.id.parent);
        title = (TextView) findViewById(R.id.title);
        image = (ImageView) findViewById(R.id.image);
        description = (TextView) findViewById(R.id.description);
        date = (TextView) findViewById(R.id.pubDate);
        duration = (TextView) findViewById(R.id.duration);
        play = (ImageView) findViewById(R.id.play);
        pause = (ImageView) findViewById(R.id.pause);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        item = getIntent().getExtras().getParcelable(MainActivity.REQUEST_INTENT);


        mediaPlayer = new MediaPlayer();
        isPlaying = false;
        pause.setVisibility(View.INVISIBLE);

        display(item);
    }


    public void display(Item item) {

        title.setText(item.getTitle());
        Picasso.with(Play.this).load(item.getImage_url()).into(image);
        description.setText(" " + item.getDescription());

        StringTokenizer stringTokenizer = new StringTokenizer(item.getDate());
        String month = "";
        String d = "";
        String year = "";
        int month_id = 0;

        stringTokenizer.nextElement();
        d = stringTokenizer.nextElement().toString();
        month = stringTokenizer.nextElement().toString();
        year = stringTokenizer.nextElement().toString();

        try {
            Date de = new SimpleDateFormat("MMM").parse(month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(de);
            month_id = cal.get(Calendar.MONTH);
            month_id = month_id + 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date.setText("Publication Date:" + "  " + month_id + "/" + d + "/" + year);
        duration.setText("Duration:" + "  " + item.getDuration());

        time = Integer.parseInt(item.getDuration())*1000;
        progressBar.setMax(time);

    }

    @Override
    public void onBackPressed() {
        if(handler!=null){
            handler.removeCallbacks(s);
        }
        if (mediaPlayer != null) {

            mediaPlayer.stop();
            mediaPlayer.release();
        }
        finish();
    }

    public void playTrack(View v) {

        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.VISIBLE);

        if (!isPlaying) {
            new PlayMedia().execute();

        } else {
            mediaPlayer.start();
        }

    }

    public void pauseTrack(View v) {

        mediaPlayer.pause();
        pause.setVisibility(View.INVISIBLE);
        play.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
    }


    private class PlayMedia extends AsyncTask<Void, Void, Void> implements MediaPlayer.OnPreparedListener {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mediaPlayer.setDataSource(item.getAudio_url());
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Play.this);
            dialog.setMessage("Loading Audio");
            dialog.setCancelable(false);
            dialog.show();

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            handler = new Handler();
            s= new HandlerSync();
            threadPool.execute(s);

            play.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
        }
    }

    private class HandlerSync implements Runnable{

        @Override
        public void run() {
            if (mediaPlayer != null) {

                int position = ((mediaPlayer.getCurrentPosition()));
                progressBar.setProgress(position);
            }
            handler.postDelayed(this, 1000);
        }

    }

}

