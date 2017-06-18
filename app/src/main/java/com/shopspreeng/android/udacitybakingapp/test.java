package com.shopspreeng.android.udacitybakingapp;

import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jayson surface on 18/06/2017.
 */

public class test {

    public static void main (String args[]){

        ArrayList<String> keys = new ArrayList<>();

        Map<String,String> mRecipeMap= new HashMap<>();
        mRecipeMap.put("cheese", "cheese");
        mRecipeMap.put("Nutela", "Nutela");
        mRecipeMap.put("YellowCake", "YellowCake");

        for(String key: mRecipeMap.keySet()){
            keys.add(key);
        }
        System.out.print(keys.get(1));
    }

}
