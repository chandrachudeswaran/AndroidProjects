package com.example.chandra.gallery500px;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class SignupActivity extends AppCompatActivity {

    EditText lastname_firstname;
    EditText email;
    EditText password;
    EditText confirm_password;
    boolean condition=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("500px Gallery Signup");
        lastname_firstname=(EditText)findViewById(R.id.lastName);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        confirm_password=(EditText)findViewById(R.id.confirmpassword);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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

    public void doRegister(View view) {

        if(lastname_firstname.getText().length()==0|| email.getText().length()==0|| password.getText().length()==0){
            makeToast("Enter all the required fields");
        }
        else if(!password.getText().toString().equals(confirm_password.getText().toString())){
            makeToast("Passwords dont match");
        }
        else if(checkEmailExists(email.getText().toString())){
            makeToast("Email already exists");
        }
        else{
            saveInParseSignUp();
        }
    }

    public void doCancelSignup(View v){
        finish();
    }

    public void makeToast(String text){
        Toast.makeText(getApplication(),text,Toast.LENGTH_SHORT).show();
    }

    public void openSearchActivity(){
        Intent intent = new Intent(SignupActivity.this,SearchActivity.class);
        startActivity(intent);
        finish();
    }


    public void saveInParseSignUp(){

        ParseUser user = new ParseUser();
        user.setUsername(email.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        String[] array = lastname_firstname.getText().toString().split(" ");
        user.put("firstname",array[0]);
        user.put("lastname",array[1]);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    makeToast("Signup Success");
                    openSearchActivity();
                } else {
                    makeToast("Signup failed");
                }
            }
        });

    }

    public boolean checkEmailExists(String email){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email", email);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    condition=true;
                } else {
                    condition = false;
                }
            }
        });
        return condition;
    }
}
