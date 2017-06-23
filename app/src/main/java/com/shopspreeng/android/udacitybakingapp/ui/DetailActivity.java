package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Ingredient;
import com.shopspreeng.android.udacitybakingapp.data.NetworkUtils;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity implements DetailActivityFragment.OnStepInteractionListener,
        IngredientFragment.OnIngredientInteractionListener, MediaPlayerFragment.OnMediaPlayerFragmentInteraction{

    private boolean isTablet;

    String recipeName;

    ArrayList<Step> stepToFrag;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(findViewById(R.id.detail_container) != null){

            isTablet = true;

            Intent intent = getIntent();
            Bundle b = intent.getExtras();

            if(savedInstanceState == null) {

                recipeName = b.getString(getString(R.string.name));

                stepToFrag = b.getParcelableArrayList(getString(R.string.steps));

            }else {
                ArrayList<Step> savedIngred = savedInstanceState.getParcelableArrayList(getString(R.string.ingredients));
                stepToFrag = savedIngred;
            }

            getSupportActionBar().setTitle(recipeName);

            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            detailActivityFragment.setSteps(stepToFrag,null);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container,detailActivityFragment)
                    .commit();

        }else {
            isTablet = false;
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.ingredients),stepToFrag);
        outState.putString(getString(R.string.name),recipeName);
    }

    @Override
    public void onStepInteraction(View view, int position, String recipe) {

        if(isTablet) {

            if (position == 0) {
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
                                .replace(R.id.detail_container, ingredientFragment)
                                .commit();

                    }
                }.execute();
            } else {

                MediaPlayerFragment mediaFragment = new MediaPlayerFragment();
                mediaFragment.setPosition(position);
                mediaFragment.setText(stepToFrag.get(position).getDesc());
                mediaFragment.setSteps(stepToFrag);
                mediaFragment.setVideoUrl(stepToFrag.get(position).getVideoUrl());

                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.detail_container, mediaFragment)
                        .commit();

            }
        }
    }

    @Override
    public void onMediaPlayerInteraction(Uri uri) {

    }

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
