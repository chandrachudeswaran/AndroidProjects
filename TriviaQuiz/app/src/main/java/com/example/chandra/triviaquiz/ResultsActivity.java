//Homework 3
//ResultsActivity.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.triviaquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {
    ArrayList<Integer> integerArrayList;
    int progress;
    int count;

    ProgressBar progressBar;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        tv = (TextView) findViewById(R.id.percentage);
        if (getIntent().getIntegerArrayListExtra("Results") != null) {
            integerArrayList = getIntent().getIntegerArrayListExtra("Results");
        }

        Log.d("answerlist",integerArrayList+"");
        for (int integer : integerArrayList) {
            if (integer == 1) {
                count++;
            }
        }

        int size = integerArrayList.size();
        progressBar.setMax(size);
        progressBar.setProgress(count);

        float percent = (float) count / size;

        percent = percent * 100;

        int p = (int) percent;
        Log.d("re", percent + "");
        tv.setText(String.valueOf(p) + "%");
    }

    public void tryAgain(View v) {

        Intent intent = new Intent(getApplicationContext(), TriviaActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToMain(View v){

        finish();
    }

    @Override
    public void onBackPressed() {


        finish();
    }


}
