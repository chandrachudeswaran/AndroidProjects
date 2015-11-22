package com.example.chandra.gallery500px;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

public class SearchActivity extends AppCompatActivity {

    EditText search_keyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("500px Gallery Search");
        search_keyword =(EditText)findViewById(R.id.search_term);
    }



    public void doSearch(View view) {
        if(search_keyword.getText().length()==0){
            makeToast("Enter the keyword");
        }
        else{
            openGalleryActivity();
        }
    }


    public void makeToast(String text){
        Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();
    }

    public void openGalleryActivity(){
        Intent intent = new Intent(SearchActivity.this,GalleryActivity.class);
        intent.putExtra("Keyword",search_keyword.getText().toString());
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();


        ParseUser user = ParseUser.getCurrentUser();
        if(user==null){
            finish();
        }
    }
}
