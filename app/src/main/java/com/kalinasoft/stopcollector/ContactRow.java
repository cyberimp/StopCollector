package com.kalinasoft.stopcollector;

import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by Ovoshi on 11/23/2016.
 */

public class ContactRow implements Serializable{
    private String number;
    private String name;
    private String photoID;

    public ContactRow(@Nullable String number, @Nullable String name, @Nullable String photoID) {
        this.name = name;
        this.number = number;
        this.photoID = photoID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

//    public Uri getPhotoURI() {
//        if (photoURI == null)
//            return null;
//        else
//            return Uri.parse(photoURI);
//    }

//    public void setPhotoURI(String photoURI) {
//        this.photoURI = photoURI;
//    }

    public String getPhotoID() {
        return photoID;
    }
}
