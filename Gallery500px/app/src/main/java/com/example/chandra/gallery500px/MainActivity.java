package com.example.chandra.gallery500px;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;

import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity {

    EditText email_text;
    EditText password_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("500px Gallery Login");
        setContentView(R.layout.activity_main);

        try {
            Parse.initialize(this, "kj4PjHzE3jarU8fjbn77zhLpIDkN1XB22XpFZSyT", "kb5MRLPnDg8r7N4vSsZv8nFtCKgQmGHq45c4zJmk");
        } catch (Exception e) {
        }


        email_text = (EditText) findViewById(R.id.email);
        password_text = (EditText) findViewById(R.id.password);

        ParseUser loggedInUser = ParseUser.getCurrentUser();

        if (loggedInUser != null) {
            openGalleryActivity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void doSignupCheck(View view) {
        openSignupActivity();
    }

    public void doLoginCheck(View view) {

        if (email_text.getText().length() == 0 || password_text.getText().length() == 0) {
            makeToast("Please enter valid credentials");
        } else {
            ParseUser.logInInBackground(email_text.getText().toString(), password_text.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {

                    if (e == null) {
                        openGalleryActivity();
                    } else {
                        makeToast("Login Failed");
                    }
                }
            });
        }
    }


    public void openGalleryActivity() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void openSignupActivity(){
        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    public void makeToast(String text) {
        Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();
    }
}
