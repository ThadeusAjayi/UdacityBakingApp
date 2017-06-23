package com.shopspreeng.android.udacitybakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jayson surface on 13/06/2017.
 */

public class Recipe implements Parcelable{

    public String mId, mName, mServings;

    protected Recipe(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mServings = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public String toString() {
        return "Recipe{" +
                "mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mServings='" + mServings + '\'' +
                '}';
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmServings() {
        return mServings;
    }

    public void setmServings(String mServings) {
        this.mServings = mServings;
    }

    public Recipe(){}


    public Recipe(String id, String name, String servings) {
        mName = name;
        mServings = servings;
        mId = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mName);
        parcel.writeString(mServings);
    }
}
