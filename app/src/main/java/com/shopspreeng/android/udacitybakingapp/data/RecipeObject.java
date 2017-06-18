package com.shopspreeng.android.udacitybakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jayson surface on 17/06/2017.
 */

public class RecipeObject extends Recipe implements Parcelable {

    ArrayList<Recipe> mIngredients;

    ArrayList<Recipe> mSteps;


    protected RecipeObject(Parcel in) {
        super();
    }

    public static final Creator<RecipeObject> CREATOR = new Creator<RecipeObject>() {
        @Override
        public RecipeObject createFromParcel(Parcel in) {
            return new RecipeObject(in);
        }

        @Override
        public RecipeObject[] newArray(int size) {
            return new RecipeObject[size];
        }
    };

    public ArrayList<Recipe> getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(ArrayList<Recipe> mIngredients) {
        this.mIngredients = mIngredients;
    }

    public ArrayList<Recipe> getmSteps() {
        return mSteps;
    }

    public void setmSteps(ArrayList<Recipe> mSteps) {
        this.mSteps = mSteps;
    }

    public RecipeObject(String name, String servings, ArrayList<Recipe> ingredients, ArrayList<Recipe> steps) {
        super(name, servings);
        mIngredients = ingredients;
        mSteps = steps;



    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
