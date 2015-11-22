//HW 5
//Item.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.tedradiohourpodcast;

import android.os.Parcelable;
import android.os.Parcel;

/**
 * Created by chandra on 10/17/2015.
 */
public class Item implements Parcelable {

    String title;
    String description;
    String image_url;
    String audio_url;
    String date;
    String duration;

    public Item() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "com.example.chandra.tedradiohourpodcast.Item{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image_url='" + image_url + '\'' +
                ", audio_url='" + audio_url + '\'' +
                ", date='" + date + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

    public Item(String title, String description, String image_url, String audio_url, String date, String duration) {
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.audio_url = audio_url;
        this.date = date;
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(image_url);
        dest.writeString(audio_url);
        dest.writeString(date);
        dest.writeString(duration);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    private Item(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.image_url = in.readString();
        this.audio_url = in.readString();
        this.date = in.readString();
        this.duration = in.readString();
    }
}
