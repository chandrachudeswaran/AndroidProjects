package com.example.chandra.gallery500px;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class GalleryActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ArrayList<Photo> photoArrayList;
    ArrayAdapter adapter;
    List<ParseObject> listFav;
    CharSequence[] users;
    LinkedHashMap<String, Integer> parse_users;
    AlertDialog.Builder builder;
    ArrayList<SharingStatus> sharingStatusList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setTitle("500px Gallery");
        listFav = new ArrayList<>();
        String keyword = getIntent().getExtras().getString("Keyword");
        parse_users = new LinkedHashMap<>();
        RequestParams requestParams = new RequestParams("https://api.500px.com/v1/photos/search", "GET");
        requestParams.addParams("consumer_key", "iozX5lQidyS8mQLlbW8WKM3nJyCXo7HO2FCKtZTI");
        requestParams.addParams("term", keyword);
        requestParams.addParams("image_size", "4");
        requestParams.addParams("rpp", "50");
        new SearchImages().execute(requestParams);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id== R.id.showall){
            display();
        }
        if (id == R.id.logout) {
            ParseUser user = ParseUser.getCurrentUser();
            if (user != null) {
                ParseUser.logOut();
                finish();
            }
        }

        if (id == R.id.showfav) {
            ParseQuery<ParseObject> photo = ParseQuery.getQuery("Photo");
            photo.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null) {
                        ArrayList<Photo> list_fav = new ArrayList<Photo>();
                        for (ParseObject obj : list) {
                            Photo photo = new Photo();
                            photo.setPhoto_title(obj.getString("title"));
                            photo.setOwner_photo(obj.getString("ownerphoto"));
                            photo.setPhoto_count(obj.getInt("count"));
                            photo.setPhoto_url(obj.getString("imageurl"));
                            photo.setOwner_name(obj.getString("ownername"));
                            list_fav.add(photo);
                        }
                        ListView listView = (ListView) findViewById(R.id.listView);
                        setTitle("Favorites");
                        adapter = new ListAdapter(GalleryActivity.this, R.layout.listrow, list_fav);
                        listView.setAdapter(adapter);
                        adapter.setNotifyOnChange(true);
                    }
                }
            });
        }


        if (id == R.id.clearfav) {
            ParseQuery<ParseObject> photo = ParseQuery.getQuery("Photo");
            photo.whereEqualTo("user", ParseUser.getCurrentUser());
            photo.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null) {
                        for (ParseObject obj : list) {
                            try {
                                obj.delete();
                                obj.saveInBackground();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                        Toast.makeText(getApplicationContext(), "Favorites cleared", Toast.LENGTH_SHORT).show();
                    }
                    ListView listView = (ListView) findViewById(R.id.listView);
                    setTitle("Gallery Activity");
                    adapter = new ListAdapter(GalleryActivity.this, R.layout.listrow, photoArrayList);
                    listView.setAdapter(adapter);
                    adapter.setNotifyOnChange(true);
                }
            });
        }

        if (id == R.id.showshared) {
            ParseQuery<ParseObject> photo = ParseQuery.getQuery("Shared");
            photo.whereEqualTo("shareduser", ParseUser.getCurrentUser());
            photo.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null) {
                        final ArrayList<Photo> list_shared = new ArrayList<Photo>();
                        for (ParseObject obj : list) {
                            final Photo photoobj = new Photo();
                            photoobj.setPhoto_title(obj.getString("title"));
                            photoobj.setOwner_photo(obj.getString("ownerphoto"));
                            photoobj.setPhoto_count(obj.getInt("count"));
                            photoobj.setPhoto_url(obj.getString("imageurl"));
                            photoobj.setOwner_name(obj.getString("ownername"));
                            ParseUser sharinguser = obj.getParseUser("usersharing");
                            ParseQuery<ParseUser> query = ParseUser.getQuery();
                            query.whereEqualTo("objectId", sharinguser.getObjectId());
                            query.getFirstInBackground(new GetCallback<ParseUser>() {
                                @Override
                                public void done(ParseUser parseUser, ParseException e) {
                                    if (e == null) {
                                        photoobj.setFirstname(parseUser.getString("firstname"));
                                        photoobj.setLastname(parseUser.getString("lastname"));
                                        list_shared.add(photoobj);
                                    }
                                    ListView listView = (ListView) findViewById(R.id.listView);
                                    setTitle("Shared with you");
                                    adapter = new ListAdapter(GalleryActivity.this, R.layout.listrow, list_shared);
                                    listView.setAdapter(adapter);
                                    adapter.setNotifyOnChange(true);
                                }
                            });
                        }
                    }
                }
            });
        }

        if (id == R.id.clearShared) {
            ParseQuery<ParseObject> photo = ParseQuery.getQuery("Shared");
            photo.whereEqualTo("shareduser", ParseUser.getCurrentUser());
            photo.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null) {
                        for (ParseObject obj : list) {
                            try {
                                obj.delete();
                                obj.saveInBackground();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                        Toast.makeText(getApplicationContext(), "Sharedlist cleared", Toast.LENGTH_SHORT).show();
                    }
                    ListView listView = (ListView) findViewById(R.id.listView);
                    setTitle("Gallery Activity");
                    adapter = new ListAdapter(GalleryActivity.this, R.layout.listrow, photoArrayList);
                    listView.setAdapter(adapter);
                    adapter.setNotifyOnChange(true);
                }
            });
        }

        if (id == R.id.sharing) {
            showSharingStatus();
        }
        return super.onOptionsItemSelected(item);
    }


    private class SearchImages extends AsyncTask<RequestParams, Void, ArrayList<Photo>> {

        @Override
        protected ArrayList<Photo> doInBackground(RequestParams... params) {
            try {
                BufferedReader reader = null;
                String line = "";
                StringBuilder sb = new StringBuilder();
                HttpURLConnection con = params[0].getConnection();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return ParseUtil.PhotoUtil.parsePhoto(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(GalleryActivity.this);
            progressDialog.setMessage("Loading Results..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Photo> photos) {
            super.onPostExecute(photos);
            progressDialog.dismiss();
            photoArrayList = photos;
            display();

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void displayAlertDialog(ArrayList<SharingStatus> list) {
        users = new CharSequence[list.size()];
        boolean cond=true;
        for (int i = 0; i < list.size(); i++) {
            users[i] = list.get(i).toString();
            if(list.get(i).getLastname()==null || list.get(i).getFirstname()==null){
                cond=false;
            }
        }
        if (users != null && cond) {
            if (users.length == list.size()) {
                builder = new AlertDialog.Builder(GalleryActivity.this);
                builder.setTitle("User Stats")
                        .setCancelable(true)
                        .setItems(users, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        }
    }

    public void display(){
        ArrayList<Photo> list_appname = photoArrayList;
        Collections.sort(list_appname, new Comparator<Photo>() {
            @Override
            public int compare(Photo o1, Photo o2) {
                return o2.getPhoto_title().compareTo(o1.getPhoto_title());
            }
        });
        photoArrayList = list_appname;
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ListAdapter(GalleryActivity.this, R.layout.listrow, photoArrayList);
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GalleryActivity.this, DetailsActivity.class);
                intent.putExtra("PhotoDetails", photoArrayList.get(position));
                startActivity(intent);
            }
        });
    }

    public void showSharingStatus() {
        sharingStatusList = new ArrayList<>();
        ParseQuery<ParseObject> shared = ParseQuery.getQuery("Shared");
        shared.whereEqualTo("shareduser", ParseUser.getCurrentUser());
        shared.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject parseObject : list) {
                        SharingStatus statusObject = new SharingStatus();
                        statusObject.setId(parseObject.getParseUser("usersharing").getObjectId());
                        if (sharingStatusList.isEmpty()) {
                            sharingStatusList.add(statusObject);
                        }
                        boolean cond = false;
                        int i = 0;
                        for (SharingStatus status : sharingStatusList) {
                            if (status.getId().equals(statusObject.getId())) {
                                cond = true;
                                break;
                            } else {
                                i++;
                            }
                        }
                        if (cond) {
                            SharingStatus obj = sharingStatusList.get(i);
                            obj.setSharing_count(obj.getSharing_count() + 1);
                            sharingStatusList.set(i, obj);
                        } else {
                            statusObject.setSharing_count(1);
                            sharingStatusList.add(statusObject);
                        }
                    }

                }

                sortDescending(sharingStatusList);
                queryUserObject(sharingStatusList);
            }
        });


    }


    public void queryUserObject(final ArrayList<SharingStatus> list){
        for(final SharingStatus status: list){
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", status.getId());
            query.getFirstInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    status.setFirstname(parseUser.getString("firstname"));
                    status.setLastname(parseUser.getString("lastname"));

                    displayAlertDialog(list);
                }

            });

        }
    }

    public ArrayList<SharingStatus> sortDescending(ArrayList<SharingStatus> sharingStatusList){

        Collections.sort(sharingStatusList, new Comparator<SharingStatus>() {
            @Override
            public int compare(SharingStatus o1, SharingStatus o2) {
                Integer i2 = o2.getSharing_count();
                Integer i1= o1.getSharing_count();
                return i2.compareTo(i1);
            }
        });

        return sharingStatusList;
    }

}
