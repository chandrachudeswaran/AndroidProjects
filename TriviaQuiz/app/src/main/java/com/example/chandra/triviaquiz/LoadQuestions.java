//Homework 3
//LoadQuestions.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.triviaquiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by chandra on 9/25/2015.
 */
public class LoadQuestions extends AsyncTask<RequestParams, Void, ArrayList<String>> {

    PassQuestions passQuestions;
    Context c;
    ProgressDialog dialog;

    public void setMainContext(Context c) {
        this.c = c;
    }

    public LoadQuestions(PassQuestions passQuestions) {
        this.passQuestions = passQuestions;
    }


    @Override
    protected ArrayList<String> doInBackground(RequestParams... params) {
        ArrayList<String> input = new ArrayList<>();
        BufferedReader reader = null;

        try {
            HttpURLConnection con = params[0].getConnection();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                input.add(line);
            }
            return input;
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
    protected void onPreExecute() {
        dialog = new ProgressDialog(c);
        dialog.setMessage("Loading Questions..");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {

        if(strings!=null) {
            ArrayList<Questions> q = new ArrayList<>();

            for (String line : strings) {
                boolean cond = false;
                String[] array = line.split(";", -1);
                Log.d("first_string", line + "" + array.length);
                if (array.length < 6) {
                    Log.d("exiting cos of length", line);
                    continue;
                } else {
                    Questions quest = new Questions();
                    int length = array.length - 1;

                    for (int i = 0; i < length - 1; i++) {
                        if (array[i].trim().equals("")) {
                            cond = true;
                            Log.d("exiting in for loop", line);
                        }
                        if (cond) {
                            break;
                        }
                    }

                    if (cond) {
                        continue;
                    }

                    boolean co = array[length - 1].equals("") || array[length - 1].contains("http");
                    if (array[length].equals("") && co) {


                        ArrayList<String> answers = new ArrayList<>();
                        for (int i = 2; i < length - 1; i++) {
                            answers.add(array[i]);
                        }

                        if (answers.size() <= 7 && answers.size() >= 2) {

                            Log.d("validation passed", line);
                            quest.setPossible_answers(answers);
                            quest.setId(Integer.parseInt(array[0]));
                            quest.setQuestion(array[1]);
                            quest.setUrl(array[length - 1]);
                            Log.d("output", quest.toString());
                            q.add(quest);
                        } else {
                            Log.d("validation failed1", line);
                            continue;
                        }
                    } else {
                        Log.d("validation failed", line);
                        continue;
                    }
                }
            }
            dialog.dismiss();
            passQuestions.sendData(q);
        }
        else{
            dialog.dismiss();
            passQuestions.sendData(null);
        }
    }


    public interface PassQuestions {

        void sendData(ArrayList<Questions> quest);

        Context getContext();
    }
}
