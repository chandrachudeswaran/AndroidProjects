//Homework 4
//MediaList.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.appstorelatest;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chandra on 10/1/2015.
 */
public class MediaList implements Parcelable{

    String title;
    String small_url;
    String large_url;
    String artist;
    String duration;
    String category;
    String releaseDate;
    String link;
    String summary;
    String price;

    public MediaList() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSmall_url() {
        return small_url;
    }

    public void setSmall_url(String small_url) {
        this.small_url = small_url;
    }

    public String getLarge_url() {
        return large_url;
    }

    public void setLarge_url(String large_url) {
        this.large_url = large_url;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MediaList{" +
                "title='" + title + '\'' +
                ", small_url='" + small_url + '\'' +
                ", large_url='" + large_url + '\'' +
                ", artist='" + artist + '\'' +
                ", duration='" + duration + '\'' +
                ", category='" + category + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", link='" + link + '\'' +
                ", summary='" + summary + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public static MediaList createMediaList(JSONObject media,String type) throws JSONException{

        MediaList mediaList1 = new MediaList();

        JSONArray images = media.getJSONArray("im:image");
        JSONObject small_image = images.getJSONObject(0);
        mediaList1.setSmall_url(small_image.getString("label"));
        mediaList1.setLarge_url(images.getJSONObject(2).getString("label"));

        JSONObject title= media.getJSONObject("im:name");
        mediaList1.setTitle(title.getString("label").replace("(Unabridged)", ""));

        mediaList1.setArtist(media.getJSONObject("im:artist").getString("label"));
        mediaList1.setCategory(media.getJSONObject("category").getJSONObject("attributes").getString("label"));
        mediaList1.setReleaseDate(media.getJSONObject("im:releaseDate").getJSONObject("attributes").getString("label"));


        if(type.equals("Books")|| type.equals("iTunes U")|| type.equals("Mac Apps") || type.equals("Podcast")) {


            mediaList1.setLink(media.getJSONObject("link").getJSONObject("attributes").getString("href"));
            if(media.has("summary")) {
                mediaList1.setSummary(media.getJSONObject("summary").getString("label"));
            }
            else{
                mediaList1.setSummary("Not Available");
            }
            mediaList1.setPrice(media.getJSONObject("im:price").getJSONObject("attributes").getString("amount"));
        }

        if(type.equals("TV Shows")){
            mediaList1.setLink(media.getJSONArray("link").getJSONObject(0).getJSONObject("attributes").getString("href"));
            mediaList1.setSummary(media.getJSONObject("summary").getString("label"));
            mediaList1.setPrice(media.getJSONObject("im:price").getJSONObject("attributes").getString("amount"));
        }

        if(type.equals("Movies")){
            mediaList1.setLink(media.getJSONArray("link").getJSONObject(0).getJSONObject("attributes").getString("href"));
            mediaList1.setPrice(media.getJSONObject("im:price").getJSONObject("attributes").getString("amount"));
        }


        if(type.equals("iOS Apps")){
            mediaList1.setLink(media.getJSONObject("link").getJSONObject("attributes").getString("href"));
            mediaList1.setPrice(media.getJSONObject("im:price").getJSONObject("attributes").getString("amount"));
        }

        if(type.equals("Audio Book")){
            mediaList1.setDuration(media.getJSONArray("link").getJSONObject(1).getJSONObject("im:duration").getString("label"));
            mediaList1.setLink(media.getJSONArray("link").getJSONObject(0).getJSONObject("attributes").getString("href"));
        }


        return mediaList1;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(small_url);
        dest.writeString(large_url);
        dest.writeString(artist);
        dest.writeString(duration);
        dest.writeString(category);
        dest.writeString(releaseDate);
        dest.writeString(link);
        dest.writeString(summary);
        dest.writeString(price);



    }

    public static final Parcelable.Creator<MediaList> CREATOR = new Parcelable.Creator<MediaList>() {
        public MediaList createFromParcel(Parcel in) {
            return new MediaList(in);
        }

        public MediaList[] newArray(int size) {
            return new MediaList[size];
        }
    };

    private MediaList(Parcel in) {
        this.title=in.readString();
        this.small_url=in.readString();
        this.large_url=in.readString();
        this.artist=in.readString();
        this.duration=in.readString();
        this.category=in.readString();
        this.releaseDate=in.readString();
        this.link=in.readString();
        this.summary=in.readString();
        this.price=in.readString();
    }


}
