//Homework 3
//CheckAnswer.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.triviaquiz;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by chandra on 9/26/2015.
 */
public class CheckAnswer extends AsyncTask<RequestParams, Void, Integer> {
    Check check;

    public CheckAnswer(Check check) {
        this.check = check;
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
            Log.d("check" ,status+"");
            return status;
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
        return null;
    }

    @Override
    protected void onPostExecute(Integer i) {
        check.sendAnswer(i);
    }

    public interface Check{
        void sendAnswer(Integer integer);
    }
}
