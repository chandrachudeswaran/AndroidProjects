package com.example.chandra.gallery500px;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by chandra on 11/14/2015.
 */
public class ListAdapter extends ArrayAdapter<Photo> {

    ArrayList<Photo> list;
    Context context;
    int resource;
    boolean condition;

    public ListAdapter(Context context, int resource, ArrayList<Photo> objects) {
        super(context, resource, objects);

        this.list = objects;
        this.resource = resource;
        this.context = context;
        this.condition = false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
        }


        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

        Picasso.with(context).load(list.get(position).getPhoto_url()).into(imageView);

        TextView name=(TextView)convertView.findViewById(R.id.sharedname);
        if(list.get(position).getFirstname()!=null && list.get(position).getLastname()!=null){
            name.setText(list.get(position).getFirstname() + " " + list.get(position).getLastname());
        }else{
            name.setText("");
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(list.get(position).getPhoto_title());
        final ImageView favView = (ImageView) convertView.findViewById(R.id.image1);


        ParseQuery<ParseObject> photoObject = ParseQuery.getQuery("Photo");
        photoObject.whereEqualTo("title", list.get(position).getPhoto_title());
        photoObject.whereEqualTo("imageurl", list.get(position).getPhoto_url());
        photoObject.whereEqualTo("ownername", list.get(position).getOwner_name());
        photoObject.whereEqualTo("ownerphoto", list.get(position).getOwner_photo());
        photoObject.whereEqualTo("count", list.get(position).getPhoto_count());
        photoObject.whereEqualTo("user", ParseUser.getCurrentUser());
        photoObject.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {

                    favView.setImageResource(R.drawable.filled_star);
                } else {
                    favView.setImageResource(R.drawable.not_filled_star);
                }
            }
        });




        favView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> photo = ParseQuery.getQuery("Photo");
                photo.whereEqualTo("user", ParseUser.getCurrentUser());
                photo.whereEqualTo("imageurl", list.get(position).getPhoto_url());
                photo.whereEqualTo("title", list.get(position).getPhoto_title());
                photo.whereEqualTo("ownername", list.get(position).getOwner_name());
                photo.whereEqualTo("ownerphoto", list.get(position).getOwner_photo());
                photo.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null) {
                            condition = true;
                        } else {
                            condition = false;
                        }

                        if (!condition) {
                            favView.setImageResource(R.drawable.filled_star);
                            ParseObject photo = new ParseObject("Photo");
                            photo.put("user", ParseUser.getCurrentUser());
                            photo.put("title", list.get(position).getPhoto_title());
                            photo.put("imageurl", list.get(position).getPhoto_url());
                            photo.put("count", list.get(position).getPhoto_count());
                            photo.put("ownername", list.get(position).getOwner_name());
                            photo.put("ownerphoto", list.get(position).getOwner_photo());
                            photo.saveInBackground();
                        } else {
                            favView.setImageResource(R.drawable.not_filled_star);
                            ParseQuery<ParseObject> photo = ParseQuery.getQuery("Photo");
                            photo.whereEqualTo("user", ParseUser.getCurrentUser());
                            photo.whereEqualTo("imageurl", list.get(position).getPhoto_url());
                            photo.whereEqualTo("title", list.get(position).getPhoto_title());
                            photo.whereEqualTo("ownername", list.get(position).getOwner_name());
                            photo.whereEqualTo("ownerphoto", list.get(position).getOwner_photo());
                            photo.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    if (e == null) {
                                        try {
                                            parseObject.delete();
                                            parseObject.saveInBackground();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }
                                    } else {
                                        Log.d("demo", "Error: " + e.getMessage());
                                    }
                                }
                            });
                        }
                    }
                });


            }
        });
        return convertView;

    }


}

