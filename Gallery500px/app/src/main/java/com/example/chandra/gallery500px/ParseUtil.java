package com.example.chandra.gallery500px;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chandra on 11/13/2015.
 */
public class ParseUtil {

    static public class PhotoUtil {

        static ArrayList<Photo> parsePhoto(String input) throws JSONException {

            ArrayList<Photo> photoArrayList = new ArrayList<>();

            JSONObject object = new JSONObject(input);
            JSONArray entry = object.getJSONArray("photos");

            for (int i = 0; i < entry.length(); i++) {
                photoArrayList.add(Photo.createPhotoObject(entry.getJSONObject(i)));
            }

            return photoArrayList;
        }

    }
}
