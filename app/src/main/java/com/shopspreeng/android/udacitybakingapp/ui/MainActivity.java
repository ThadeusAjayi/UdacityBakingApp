package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Ingredient;
import com.shopspreeng.android.udacitybakingapp.data.NetworkUtils;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.shopspreeng.android.udacitybakingapp.R.string.steps;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements
        MainRecipeFragment.OnRecipeClickListener, DetailActivityFragment.OnStepInteractionListener,
        IngredientFragment.OnIngredientInteractionListener, MediaPlayerFragment.OnMediaPlayerFragmentInteraction {

    public boolean isTablet;

    MainRecipeAdapter mMainAdapter;

    DetailAdapter mDetailAdapter;

    ArrayList<Step> stepList;

    RecyclerView mRecycler;

    RecyclerView mDetailRecycler;

    String recipeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.recipe_list) != null){
            isTablet = true;

            mRecycler = (RecyclerView) findViewById(R.id.recipe_recycler);

            mMainAdapter = new MainRecipeAdapter(this, new ArrayList<Recipe>());

            if(savedInstanceState != null){
                ArrayList<Step> savedStep = savedInstanceState.getParcelableArrayList(getString(R.string.steps));
                stepList = savedStep;
            }

        }else {

            isTablet = false;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.steps),stepList);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    @Override
    public void onRecipeClick(View view, int position,final String recipe) {

        recipeName = recipe;

        if(isTablet){
            new AsyncTask<Void, Void, ArrayList<Step>>() {
                @Override
                protected ArrayList<Step> doInBackground(Void... voids) {
                    ArrayList<Step> result = new ArrayList<>();
                    try {
                        result.add(0,null);
                        result.addAll(NetworkUtils.extractStepsFromJson(run(NetworkUtils.buildBaseUrl().toString()),recipe));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return result;
                }

                @Override
                protected void onPostExecute(ArrayList<Step> step) {
                    super.onPostExecute(step);

                    stepList = step;

                    mDetailRecycler = (RecyclerView) findViewById(R.id.detail_recycler);

                    mDetailAdapter = new DetailAdapter(getApplicationContext(), new ArrayList<Step>());

                    DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
                    detailActivityFragment.setSteps(step, recipe);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.step_list,detailActivityFragment)
                            .commit();
                }
            }.execute();

        }else {
            new AsyncTask<Void, Void, ArrayList<Step>>() {
                @Override
                protected ArrayList<Step> doInBackground(Void... voids) {
                    ArrayList<Step> result = new ArrayList<>();
                    try {
                        result.add(0,null);
                        result.addAll(NetworkUtils.extractStepsFromJson(run(NetworkUtils.buildBaseUrl().toString()),recipe));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return result;
                }

                @Override
                protected void onPostExecute(ArrayList<Step> step) {
                    super.onPostExecute(step);
                    Toast.makeText(getApplicationContext(), "fragment click", Toast.LENGTH_SHORT).show();
                    Intent detailIntent = new Intent(getApplicationContext(),DetailActivity.class);
                    Bundle b = new Bundle();
                    b.putString(getString(R.string.name),recipe);
                    b.putParcelableArrayList(getString(steps),step);
                    detailIntent.putExtras(b);
                    startActivity(detailIntent);
                }
            }.execute();
        }

    }


    //Details frag for steps
    @Override
    public void onStepInteraction(View view, int position, final String recipe) {

        if(isTablet){

            if(position == 0) {
                new AsyncTask<Void, Void, ArrayList<Ingredient>>() {
                    @Override
                    protected ArrayList<Ingredient> doInBackground(Void... voids) {

                        ArrayList<Ingredient> result = new ArrayList<>();
                        try {
                            result = NetworkUtils.extractIngredientsFromJson(run(NetworkUtils.buildBaseUrl().toString()), recipeName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return result;
                    }

                    @Override
                    protected void onPostExecute(ArrayList<Ingredient> ingredient) {
                        super.onPostExecute(ingredient);

                        IngredientFragment ingredientFragment = new IngredientFragment();
                        ingredientFragment.setIngredients(ingredient);

                        getSupportFragmentManager().beginTransaction()
                                .addToBackStack(null)
                                .add(R.id.step_list, ingredientFragment)
                                .commit();

                    }
                }.execute();
            }else {

                MediaPlayerFragment mediaFragment = new MediaPlayerFragment();
                mediaFragment.setPosition(position);
                mediaFragment.setText(stepList.get(position).getDesc());
                mediaFragment.setSteps(stepList);
                mediaFragment.setVideoUrl(stepList.get(position).getVideoUrl());

                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.step_list, mediaFragment)
                        .commit();

            }
        }else {


        }
    }


    //set actions for mediaplayer fragment in main activity
    @Override
    public void onMediaPlayerInteraction(Uri uri) {

    }

    //Set actions on ingredient fragment in mainactivity
    @Override
    public void onIngredientInteraction(ArrayList<Ingredient> ingredients) {

    }

    OkHttpClient connect = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = connect.newCall(request).execute();

        String result = response.body().string();

        return result;
    }
}
