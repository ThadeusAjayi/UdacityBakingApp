package com.shopspreeng.android.udacitybakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jayson surface on 18/06/2017.
 */

public class Ingredient extends Recipe implements Parcelable{
    protected Ingredient(Parcel in) {
        mQty = in.readString();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };


    //TODO reformat toString function to show on step fragment
    @Override
    public String toString() {
        return "Ingredient{" +
                "mQty='" + mQty + '\'' +
                ", mMeasure='" + mMeasure + '\'' +
                ", mIngredient='" + mIngredient + '\'' +
                '}';
    }

    public static String getFormattedIngredients(ArrayList<Ingredient> ingredients){
        int ingredCount = 1;
        String formattedIngred = "";
        for(int i = 0; i < ingredients.size(); i ++) {
            formattedIngred = String.valueOf(ingredCount) + "\t" +ingredients.get(i).getmIngredient() + "\n";
            formattedIngred += "\t" +ingredients.get(i).getmQty() + " " + ingredients.get(i).getmIngredient();
            ingredCount++;
        }
        return formattedIngred;
    }

    String mQty, mMeasure, mIngredient;

    public String getmQty() {
        return mQty;
    }

    public void setmQty(String mQty) {
        this.mQty = mQty;
    }

    public String getmMeasure() {
        return mMeasure;
    }

    public void setmMeasure(String mMeasure) {
        this.mMeasure = mMeasure;
    }

    public String getmIngredient() {
        return mIngredient;
    }

    public void setmIngredient(String mIngredient) {
        this.mIngredient = mIngredient;
    }

    public Ingredient(String qty, String measure, String ingredient){
        mQty = qty;
        mMeasure = measure;
        mIngredient = ingredient;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mQty);
        parcel.writeString(mMeasure);
        parcel.writeString(mIngredient);
    }
}
