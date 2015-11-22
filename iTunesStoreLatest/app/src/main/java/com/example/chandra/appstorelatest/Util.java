//Homework 4
//Util.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.appstorelatest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.ArrayList;

/**
 * Created by chandra on 10/1/2015.
 */
public class Util {


    static public class MediaListParser{

        static ArrayList<MediaList> parseMedia(String input,String title) throws JSONException{
            ArrayList<MediaList> mediaList = new ArrayList<>();

            JSONObject root = new JSONObject(input);
            JSONObject feed = root.getJSONObject("feed");
            JSONArray entry = feed.getJSONArray("entry");

            for(int i =0;i<entry.length();i++){

                JSONObject media = entry.getJSONObject(i);
                MediaList mediaList1 = MediaList.createMediaList(media,title);
                mediaList.add(mediaList1);
            }
            return mediaList;

        }
    }
}
