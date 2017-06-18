package com.shopspreeng.android.udacitybakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jayson surface on 13/06/2017.
 */

public class Recipe {

    public String mId, mName, mServings;

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




}
