//HW 5
//MainActivityUtility.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.tedradiohourpodcast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chandra on 10/18/2015.
 */
public class MainActivityUtility {
    ProgressBar progressBar;
    ImageView playMedia;
    ImageView pauseMedia;
    MediaPlayer mediaPlayer;
    boolean isPlaying;
    public Activity activity;
    String audio;
    ProgressDialog dialog;
    Handler handler;
    HandlerSync s;
    ExecutorService threadPool;
    int time;

    public MainActivityUtility(Activity activity) {
        this.activity = activity;
        progressBar = (ProgressBar) this.activity.findViewById(R.id.progressbar);
        playMedia = (ImageView) this.activity.findViewById(R.id.imageViewPlayButton);
        pauseMedia = (ImageView) this.activity.findViewById(R.id.imageViewPauseButton);
    }


    public void display(String url,String duration) {
        audio = url;


        progressBar.setVisibility(View.VISIBLE);
        pauseMedia.setVisibility(View.VISIBLE);
        isPlaying = false;
        mediaPlayer = new MediaPlayer();
        threadPool = Executors.newFixedThreadPool(1);
        time = Integer.parseInt(duration)*1000;
        progressBar.setMax(time);

        if (!isPlaying) {
            new PlayMedia().execute();

        } else {
            mediaPlayer.start();
        }
    }

    private class PlayMedia extends AsyncTask<Void, Void, Void> implements MediaPlayer.OnPreparedListener {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mediaPlayer.setDataSource(audio);
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
            dialog = new ProgressDialog(activity);
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
            s = new HandlerSync();
            threadPool.execute(s);
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

    public void playMedia(View v){
        playMedia.setVisibility(View.INVISIBLE);
        pauseMedia.setVisibility(View.VISIBLE);
        mediaPlayer.start();
    }

    public void pauseMedia(View v){
        playMedia.setVisibility(View.VISIBLE);
        pauseMedia.setVisibility(View.INVISIBLE);
        mediaPlayer.pause();
    }

    public void back(){
        if(handler!=null){
            handler.removeCallbacks(s);
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

    }

    public void reset(){
        pauseMedia.setVisibility(View.INVISIBLE);
        playMedia.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        back();
    }

}
