//Homework 3
//MainActivity.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.triviaquiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity implements DeleteQuestions.DeleteTask {

    AlertDialog alertDialog;
    int delete_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void createQuestions(View v){

        Intent intent = new Intent(MainActivity.this,CreateQuestionActivity.class);
        startActivity(intent);
    }

    public void startTrivia(View v){
        Intent intent = new Intent(MainActivity.this,TriviaActivity.class);
        startActivity(intent);
    }


    public void deleteQuestions(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Questions")
                .setCancelable(false)
                .setMessage("Are you sure you want to delete all your questions?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (isConnected()) {
                                    RequestParams params = new RequestParams(TriviaAppConstants.DELETE_LINK, TriviaAppConstants.METHOD_POST);
                                    params.addParams(TriviaAppConstants.GROUP_LABEL, TriviaAppConstants.GROUP_VALUE);
                                    DeleteQuestions d = new DeleteQuestions(MainActivity.this);
                                    d.setMainContext(getContext());
                                    d.execute(params);

                                } else {
                                    Toast.makeText(getApplicationContext(), TriviaAppConstants.NO_INTERNET, Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }

                            }
                        }

                )
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }
                );

        alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public Context getContext() {
        return this;
    }

    public void exitApp(View v) {
        finish();
    }

    @Override
    public void sendDeleteStatus(int status) {
        delete_status = status;
        Log.d("demo", delete_status + "");
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net != null && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
