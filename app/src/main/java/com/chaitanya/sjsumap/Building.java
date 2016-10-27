package com.chaitanya.sjsumap;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 33843 on 10/22/2016.
 */
public class Building implements Parcelable {

    public Coordinate start;
    public Coordinate end;
    public String name;
    public String address;
    public int id;
    public int image;
    //Declare all variable related to building here.

    public Building(int id,int image, String name,String address, Coordinate start, Coordinate end){
        this.start = start;
        this.end = end;
        this.name = name;
        this.address = address;
        this.id = id;
        this.image = image;

        //Initilaize all variables here.
    }


    public boolean checkTouch(int x, int y){
        return this.start.x < x && this.end.x > x && this.start.y < y && this.end.y >y;
    }

    //Methods implemented

    protected Building(Parcel in) {
        name = in.readString();
        address = in.readString();
        id = in.readInt();
        image = in.readInt();
    }

    public static final Creator<Building> CREATOR = new Creator<Building>() {
        @Override
        public Building createFromParcel(Parcel in) {
            return new Building(in);
        }

        @Override
        public Building[] newArray(int size) {
            return new Building[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeInt(id);
        dest.writeInt(image);
    }
}
