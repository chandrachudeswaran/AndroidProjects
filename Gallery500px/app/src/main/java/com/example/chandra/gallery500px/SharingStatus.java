package com.example.chandra.gallery500px;

import android.util.Log;

/**
 * Created by chandra on 11/21/2015.
 */
public class SharingStatus {

    String id;
    String firstname;
    String lastname;
    int sharing_count;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getSharing_count() {
        return sharing_count;
    }

    public void setSharing_count(int sharing_count) {
        this.sharing_count = sharing_count;
    }

    @Override
    public String toString() {
        return
              firstname + " " +lastname+ " " + "("+ sharing_count+ ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SharingStatus other = (SharingStatus) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}
