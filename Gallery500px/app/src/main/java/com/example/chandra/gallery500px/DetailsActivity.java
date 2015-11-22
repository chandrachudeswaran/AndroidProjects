package com.example.chandra.gallery500px;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    TextView title;
    ImageView photo;
    TextView count;
    ImageView owner_image;
    TextView owner_name;
    CharSequence[] users;
    List<ParseUser> users_list;
    Photo photoDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle("500px Gallery Details");
        title = (TextView) findViewById(R.id.title);
        photo = (ImageView) findViewById(R.id.image);
        count = (TextView) findViewById(R.id.viewCount);
        owner_image = (ImageView) findViewById(R.id.owner_image);
        owner_name = (TextView) findViewById(R.id.full_name);
        users_list = new ArrayList<>();

        photoDetails = getIntent().getExtras().getParcelable("PhotoDetails");
        Picasso.with(DetailsActivity.this).load(photoDetails.getPhoto_url()).into(photo);
        title.setText(photoDetails.getPhoto_title());
        count.setText("Total Views :" + "" + photoDetails.getPhoto_count());
        owner_name.setText(photoDetails.getOwner_name());
        Picasso.with(DetailsActivity.this).load(photoDetails.getOwner_photo()).into(owner_image);

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public void doShare(View v) {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                users = new CharSequence[list.size()];
                users_list = list;
                for (int i = 0; i < list.size(); i++) {
                    users[i] = list.get(i).getString("firstname") + " "+ list.get(i).getString("lastname");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setTitle("Users")
                        .setCancelable(true)
                        .setItems(users, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {

                                ParseQuery<ParseObject> photo = ParseQuery.getQuery("Shared");
                                photo.whereEqualTo("usersharing", ParseUser.getCurrentUser());
                                photo.whereEqualTo("imageurl", photoDetails.getPhoto_url());
                                photo.whereEqualTo("title", photoDetails.getPhoto_title());
                                photo.whereEqualTo("ownername", photoDetails.getOwner_name());
                                photo.whereEqualTo("ownerphoto", photoDetails.getOwner_photo());
                                photo.whereEqualTo("shareduser",users_list.get(which));
                                photo.whereEqualTo("count",photoDetails.getPhoto_count());
                                photo.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject parseObject, ParseException e) {
                                        if(e==null){
                                            Toast.makeText(getApplicationContext(),"Already shared with this user",Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else{
                                            ParseObject parseObject1 = new ParseObject("Shared");
                                            parseObject1.put("usersharing", ParseUser.getCurrentUser());
                                            parseObject1.put("imageurl", photoDetails.getPhoto_url());
                                            parseObject1.put("title", photoDetails.getPhoto_title());
                                            parseObject1.put("ownername", photoDetails.getOwner_name());
                                            parseObject1.put("ownerphoto", photoDetails.getOwner_photo());
                                            parseObject1.put("shareduser",users_list.get(which));
                                            parseObject1.put("count",photoDetails.getPhoto_count());

                                            parseObject1.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if(e==null){
                                                        Toast.makeText(getApplicationContext(),"Shared successfully",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });


                            }
                        });
                builder.create().show();
            }
        });


    }


}
