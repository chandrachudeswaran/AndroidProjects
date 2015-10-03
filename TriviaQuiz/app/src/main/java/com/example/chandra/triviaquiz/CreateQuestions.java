//Homework 3
//CreateQuestions.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.triviaquiz;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by chandra on 9/24/2015.
 */
public class CreateQuestions extends AsyncTask<RequestParams, Void, Integer> {

    Creation creation;

    public CreateQuestions(Creation creation) {
        this.creation = creation;
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
        } finally {
            if (reader != null) {
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
        Log.d("create" ,integer+"");
        creation.sendStatus(integer);
    }

    public interface Creation {
        void sendStatus(Integer i);
    }
}
