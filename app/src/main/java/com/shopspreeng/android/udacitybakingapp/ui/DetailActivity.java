package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

import static com.shopspreeng.android.udacitybakingapp.R.bool.isTablet;
import static com.shopspreeng.android.udacitybakingapp.R.string.position;

public class DetailActivity extends AppCompatActivity implements DetailActivityFragment.OnStepInteractionListener,
        MediaPlayerFragment.OnMediaPlayerFragmentInteraction {

    String recipeName;

    ArrayList<Step> steps;

    boolean tabletSize;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabletSize = getResources().getBoolean(isTablet);

        Intent intent = getIntent();

        if(savedInstanceState == null) {

            recipeName = intent.getStringExtra(getString(R.string.name));

            Recipe getRecipe = intent.getParcelableExtra(getString(R.string.recipe_list));

            steps = NetworkUtils.extractStepsFromJson(getRecipe.getmSteps());

        }else {
            recipeName = savedInstanceState.getString(getString(R.string.name));

            steps = savedInstanceState.getParcelableArrayList(getString(R.string.steps));
        }

        getSupportActionBar().setTitle(recipeName);

        DetailActivityFragment detailActivityFragment = new DetailActivityFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        //TODO create a method to convert parcelable intent and convert to ArrayList of Ingredients and Steps


        if(tabletSize) {

            MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
            mediaPlayerFragment.setPosition(1);
            mediaPlayerFragment.setText(steps.get(1).getDesc());
            mediaPlayerFragment.setSteps(steps);
            mediaPlayerFragment.setVideoUrl(steps.get(1).getVideoUrl());

            fragmentManager.beginTransaction()
                    .add(R.id.detail_container,detailActivityFragment)
                    .add(R.id.sub_container, mediaPlayerFragment)
                    .commit();
        }else {

            detailActivityFragment.setStepsList(steps);
            Recipe getRecipe = getIntent().getParcelableExtra(getString(R.string.recipe_list));
            ArrayList<Ingredient> toDetail = NetworkUtils.extractIngredientsFromJson(getRecipe.getmIngredients());
            detailActivityFragment.setIngredientList(toDetail);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, detailActivityFragment)
                    .commit();

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.steps), steps);
        outState.putString(getString(R.string.name),recipeName);
    }

    @Override
    public void onStepInteraction(View view, int position, String recipe) {

        if(tabletSize) {

                MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
                mediaPlayerFragment.setPosition(position);
                mediaPlayerFragment.setText(steps.get(position).getDesc());
                mediaPlayerFragment.setSteps(steps);
                mediaPlayerFragment.setVideoUrl(steps.get(position).getVideoUrl());

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.sub_container, mediaPlayerFragment)
                        .commit();


        }else {

                Intent intent = new Intent(this,PhoneMediaPlayerActivity.class);
                intent.putParcelableArrayListExtra(getString(R.string.steps),steps);
                intent.putExtra(getString(R.string.name),recipe);
                intent.putExtra(getString(position),position);
                startActivity(intent);

        }
    }

    @Override
    public void onMediaPlayerInteraction(Uri uri) {

    }

}
