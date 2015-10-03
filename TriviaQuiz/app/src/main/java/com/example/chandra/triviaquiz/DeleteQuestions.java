//Homework 3
//DeleteQuestions.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.triviaquiz;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by chandra on 9/23/2015.
 */
public class DeleteQuestions extends AsyncTask<RequestParams, Void, Integer> {

    AlertDialog alertDialog;
    DeleteTask deleteTask;
    Context c;

    public DeleteQuestions(DeleteTask deleteTask) {
        this.deleteTask = deleteTask;
    }

    public void setMainContext(Context c) {
        this.c = c;
    }

    @Override
    protected void onPreExecute() {

        LinearLayout linearLayout = new LinearLayout(c);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        ProgressBar progressBar = new ProgressBar(c);
        progressBar.setLayoutParams(layoutParams);

        TextView textView1 = new TextView(c);
        textView1.setLayoutParams(layoutParams);
        textView1.setTextAppearance(c, android.R.style.TextAppearance_Large);
        textView1.setText("            ");

        TextView textView = new TextView(c);
        textView.setLayoutParams(layoutParams);
        textView.setTextAppearance(c, android.R.style.TextAppearance_Large);
        textView.setText("Deleting..");

        linearLayout.addView(progressBar);
        linearLayout.addView(textView1);
        linearLayout.addView(textView);

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Deleting Questions")
                .setCancelable(false)
                .setView(linearLayout);

        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected Integer doInBackground(RequestParams... params) {
        BufferedReader reader = null;
        int status = 0;
        String line = "";
        try {
            HttpURLConnection con = params[0].getConnection();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            if ((line = reader.readLine()) != null) {
                status = Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return status;
    }

    @Override
    protected void onPostExecute(Integer integer) {

        alertDialog.dismiss();
        deleteTask.sendDeleteStatus(integer);
    }

    public interface DeleteTask {

        void sendDeleteStatus(int status);

        Context getContext();


    }
}
