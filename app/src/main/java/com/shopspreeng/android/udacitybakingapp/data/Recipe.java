package com.shopspreeng.android.udacitybakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jayson surface on 13/06/2017.
 */

public class Recipe {

    private String mName, mServings;

    private String mQty, mMeasure, mIngredient;

    private String msDesc, mDesc, mVideo, mThumb;


    public Recipe(String name, String servings) {
        mName = name;
        mServings = servings;
    }

    public Recipe(String qty, String measure, String ingredient) {
        mQty = qty;
        mMeasure = measure;
        mIngredient = ingredient;
    }

    public Recipe() {

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

    public String getMsDesc() {
        return msDesc;
    }

    public void setMsDesc(String msDesc) {
        this.msDesc = msDesc;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmVideo() {
        return mVideo;
    }

    public void setmVideo(String mVideo) {
        this.mVideo = mVideo;
    }

    public String getmThumb() {
        return mThumb;
    }

    public void setmThumb(String mThumb) {
        this.mThumb = mThumb;
    }

    public Recipe(String sDesc, String desc, String video, String thumb) {
        msDesc = sDesc;
        mDesc = desc;
        mVideo = video;
        mThumb = thumb;
    }

}
