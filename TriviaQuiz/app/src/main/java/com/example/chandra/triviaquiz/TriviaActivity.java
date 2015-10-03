//Homework 3
//TriviaActivity.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.triviaquiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity implements LoadQuestions.PassQuestions, LoadImage.ImageLoading, CheckAnswer.Check {
    int index = 0;
    int size = 0;
    ArrayList<Questions> questions;
    ArrayList<Integer> answers = new ArrayList<>();
    RadioGroup rg;
    boolean nullFlag = false;
    ImageView imageView;
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        imageView = (ImageView) findViewById(R.id.imageURL);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        textView = (TextView) findViewById(R.id.textView);

        RequestParams params = new RequestParams(TriviaAppConstants.GET_ALL_LINK, TriviaAppConstants.METHOD_GET);
        LoadQuestions loadQuestions = new LoadQuestions(this);
        loadQuestions.setMainContext(getContext());
        loadQuestions.execute(params);
    }

    @Override
    public void sendData(ArrayList<Questions> quest) {

        if (quest != null) {
            questions = quest;
            size = quest.size();

            Questions q = quest.get(index);
            displayData(q);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.images);
            imageView.setVisibility(View.VISIBLE);
            nullFlag=true;
            TextView tv = (TextView) findViewById(R.id.question);
            tv.setText("No questions available! Try Creating new or try later!!");
        }
    }

    public void displayData(Questions q) {



        TextView qid = (TextView) findViewById(R.id.questionID);
        qid.setText("Q" + String.valueOf(index + 1));
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (!q.getUrl().equals("")) {

            RequestParams params = new RequestParams(q.getUrl(), TriviaAppConstants.METHOD_GET);
            new LoadImage(this).execute(params);

        } else {

            progressBar.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.images);
            imageView.setVisibility(View.VISIBLE);
        }

        TextView tv = (TextView) findViewById(R.id.question);
        tv.setText(q.getQuestion());

        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.removeAllViews();
        for (int i = 0; i < q.getPossible_answers().size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(q.getPossible_answers().get(i));
            radioButton.setLayoutParams(layoutParams);
            rg.addView(radioButton);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void sendImage(Bitmap image) {

        progressBar.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        imageView.setImageBitmap(image);
        imageView.setVisibility(View.VISIBLE);

    }

    public void goNext(View v) {




            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);

            int answer_index = 0;
            boolean selected = false;
            RequestParams params = new RequestParams(TriviaAppConstants.CHECK_ANSWER_LINK, TriviaAppConstants.METHOD_POST);
            params.addParams(TriviaAppConstants.GROUP_LABEL, TriviaAppConstants.GROUP_VALUE);
            params.addParams("qid", String.valueOf(questions.get(index).getId()));
            for (int i = 0; i < rg.getChildCount(); i++) {
                RadioButton rb = (RadioButton) rg.getChildAt(i);
                if (rb.isChecked()) {
                    Log.d("index", i + "");
                    answer_index = i;
                    selected = true;
                    break;
                } else {
                    continue;
                }
            }

            if (selected) {
                params.addParams("a", String.valueOf(answer_index));
            } else {
                params.addParams("a", String.valueOf(100));
            }

            new CheckAnswer(this).execute(params);

        }

    @Override
    public void sendAnswer(Integer i) {
        answers.add(i);
        if(i==0){
            Toast.makeText(getApplicationContext(),"Answer is incorrect",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Answer is correct",Toast.LENGTH_SHORT).show();
        }
        if(nullFlag){
            progressBar.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.drawable.images);
            imageView.setVisibility(View.VISIBLE);
            TextView tv = (TextView) findViewById(R.id.question);
            tv.setText("No questions available! Try Creating new or try later!!");
            return;
        }
        index++;
        if (index > size - 1 ) {

            Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
            intent.putIntegerArrayListExtra("Results", answers);
            startActivity(intent);
            finish();
        } else {
            Questions q = questions.get(index);

                displayData(q);

        }

    }

    public void quitToMain(View v) {
        finish();
    }

    @Override
    public void onBackPressed() {


        finish();
    }
}
