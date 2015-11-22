package com.example.chandra.gallery500px;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Parcelable;
import android.os.Parcel;

/**
 * Created by chandra on 11/13/2015.
 */
public class Photo implements Parcelable{

    String photo_title;
    String photo_url;
    int photo_count;
    String owner_name;
    String owner_photo;

    String firstname;
    String lastname;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoto_title() {
        return photo_title;
    }

    public void setPhoto_title(String photo_title) {
        this.photo_title = photo_title;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public int getPhoto_count() {
        return photo_count;
    }

    public void setPhoto_count(int photo_count) {
        this.photo_count = photo_count;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_photo() {
        return owner_photo;
    }

    public void setOwner_photo(String owner_photo) {
        this.owner_photo = owner_photo;
    }

    public Photo() {
    }

    @Override
    public String toString() {
        return "Photo{" +
                "photo_title='" + photo_title + '\'' +
                ", photo_url='" + photo_url + '\'' +
                ", photo_count=" + photo_count +
                ", owner_name='" + owner_name + '\'' +
                ", owner_photo='" + owner_photo + '\'' +
                '}';
    }

    public static Photo createPhotoObject(JSONObject obj) throws JSONException{
        Photo photo = new Photo();
        JSONArray array = obj.getJSONArray("images");
        JSONObject obj1 = array.getJSONObject(0);

        photo.setPhoto_url(obj1.getString("url"));

        photo.setPhoto_title(obj.getString("name"));
        char first = Character.toUpperCase(photo.getPhoto_title().charAt(0));
        photo.setPhoto_title(first + photo.getPhoto_title().substring(1));
        photo.setPhoto_count(Integer.valueOf(obj.getString("times_viewed")));
        photo.setOwner_name(obj.getJSONObject("user").getString("fullname"));
        photo.setOwner_photo(obj.getJSONObject("user").getString("userpic_url"));
        return photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photo_title);
        dest.writeString(photo_url);
        dest.writeInt(photo_count);
        dest.writeString(owner_name);
        dest.writeString(owner_photo);


    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    private Photo(Parcel in) {
        this.photo_title = in.readString();
        this.photo_url = in.readString();
        this.photo_count = in.readInt();
        this.owner_name = in.readString();
        this.owner_photo = in.readString();

    }
}
