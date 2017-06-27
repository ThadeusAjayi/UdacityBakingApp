package com.shopspreeng.android.udacitybakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jayson surface on 13/06/2017.
 */

public class Recipe implements Parcelable{

    public String mName, mIngredients, mSteps;

    protected Recipe(Parcel in) {
        mName = in.readString();
        mIngredients = in.readString();
        mSteps = in.readString();
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
                "mName='" + mName + '\'' +
                ", mIngredients='" + mIngredients + '\'' +
                ", mSteps='" + mSteps + '\'' +
                '}';
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(String mIngredients) {
        this.mIngredients = mIngredients;
    }

    public String getmSteps() {
        return mSteps;
    }

    public void setmSteps(String mSteps) {
        this.mSteps = mSteps;
    }

    public Recipe(){}


    public Recipe(String name, String ingredients, String steps) {
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mIngredients);
        parcel.writeString(mSteps);
    }
}
