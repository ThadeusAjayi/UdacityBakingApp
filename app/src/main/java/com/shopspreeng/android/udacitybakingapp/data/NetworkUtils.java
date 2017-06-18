package com.shopspreeng.android.udacitybakingapp.data;

import android.net.Uri;

import com.shopspreeng.android.udacitybakingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.provider.CalendarContract.CalendarCache.URI;

/**
 * Created by jayson surface on 13/06/2017.
 */

public class NetworkUtils {

    public static final String BASE_URL = "http://go.udacity.com/android-baking-app-json";

    public static URL buildBaseUrl(){

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static Map<String,ArrayList<RecipeObject>> extractFromJson(String recipeJson){

        Map<String,ArrayList<RecipeObject>> jsonResult = new HashMap<>();
        ArrayList<RecipeObject> resultRecipeObject = new ArrayList<>();
        ArrayList<Recipe> recipeIngredient = new ArrayList<>();
        ArrayList<Recipe> recipeStep = new ArrayList<>();

        //Temp holder variables
        Recipe ingredientList;
        Recipe stepList;
        String name ;

        try {
            JSONArray recipeArray = new JSONArray(recipeJson);
            for(int i = 0; i < recipeArray.length(); i++) {
                JSONObject recipeObject = recipeArray.getJSONObject(i);
                name = recipeObject.getString("name");
                String servings = recipeObject.getString("servings");
                JSONArray ingredients = recipeObject.getJSONArray("ingredients");
                    for(int j = 0; j < ingredients.length(); j++){
                        JSONObject ingredientObject = ingredients.getJSONObject(j);
                        String quantity = ingredientObject.getString("quantity");
                        String measure = ingredientObject.getString("measure");
                        String ingredient = ingredientObject.getString("ingredient");

                        ingredientList = new Recipe(quantity,measure,ingredient);
                        recipeIngredient.add(ingredientList);
                    }
                JSONArray steps = recipeObject.getJSONArray("steps");
                    for(int j = 0; j < steps.length(); j++) {
                        JSONObject stepsObject = steps.getJSONObject(j);
                        String sDesc = stepsObject.getString("shortDescription");
                        String desc = stepsObject.getString("description");
                        String video = stepsObject.getString("videoURL");
                        String thumb = stepsObject.getString("thumbnailURL");

                        stepList = new Recipe(sDesc,desc,video,thumb);
                        recipeStep.add(stepList);

                }
                String mapKey = name;
                RecipeObject recipeList = new RecipeObject(name,servings,recipeIngredient,recipeStep);
                resultRecipeObject.add(recipeList);
                jsonResult.put(mapKey,resultRecipeObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonResult;

    }

}
