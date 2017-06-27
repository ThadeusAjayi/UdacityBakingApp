package com.shopspreeng.android.udacitybakingapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jayson surface on 26/06/2017.
 */

public class DatabaseUtils {

    //pass data here before recipe bulk insert
    public ContentValues[] getRecipeCvArray(ArrayList<Recipe> recipeArrayList){

        ContentValues [] result = new ContentValues[recipeArrayList.size()];

        ContentValues contentValues;

        int count = 0;
        for(Recipe a : recipeArrayList){
            contentValues = new ContentValues();
            contentValues.put(BakingContract.BakingEntry.RECIPE,a.getmName());
            contentValues.put(BakingContract.BakingEntry.INGREDIENTS,a.getmIngredients());
            contentValues.put(BakingContract.BakingEntry.STEPS,a.getmSteps());
            result[count] = contentValues;
            count++;
        }
        return result;
    }

    public ArrayList<Recipe> cursorToArrayListRecipe(Cursor cursor){

        ArrayList<Recipe> result = new ArrayList<>();
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            String name = cursor.getString(cursor.getColumnIndex(BakingContract.BakingEntry.RECIPE));
            String ingredient = cursor.getString(cursor.getColumnIndex(BakingContract.BakingEntry.INGREDIENTS));
            String step = cursor.getString(cursor.getColumnIndex(BakingContract.BakingEntry.STEPS));

            result.add(new Recipe(name,ingredient,step));

        }
        cursor.close();

        return result;
    }

}
