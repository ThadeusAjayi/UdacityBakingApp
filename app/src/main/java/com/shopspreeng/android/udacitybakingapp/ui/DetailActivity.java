package com.shopspreeng.android.udacitybakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.shopspreeng.android.udacitybakingapp.BakingAppWidgetProvider;
import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.DataUtils;
import com.shopspreeng.android.udacitybakingapp.data.Ingredient;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.util.ArrayList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.shopspreeng.android.udacitybakingapp.R.bool.isTablet;

public class DetailActivity extends AppCompatActivity implements DetailActivityFragment.OnStepInteractionListener,
        MediaPlayerFragment.OnMediaPlayerFragmentInteraction, SharedPreferences.OnSharedPreferenceChangeListener {

    String recipeName;

    ArrayList<Step> steps;

    Menu menu;

    boolean tabletSize;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.fav:
                setFavPreference(item);
                Toast.makeText(this, "Saved favourite", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public int setIconOnSelect(MenuItem item){
        return R.drawable.chefselect;
    }

    public void setFavPreference(MenuItem item){
        SharedPreferences fav = getDefaultSharedPreferences(this);
//TODO try edit.apply() and move code to on sharepreferece changed
        SharedPreferences.Editor edit = fav.edit();
        edit.putString(getString(R.string.pref_default_key),recipeName);
        edit.commit();

        fav.registerOnSharedPreferenceChangeListener(this);

        item.setIcon(setIconOnSelect(item));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(),BakingAppWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.baking_list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fav_menu,menu);
        this.menu = menu;
       // updateMenuTitles();
        return true;
    }

    private void updateMenuTitles() {
        MenuItem menuIcon = menu.findItem(R.id.fav);
        String fav = getDefaultSharedPreferences(this).getString(getString(R.string.pref_default_key),
                getString(R.string.value));
        if (fav.equals(recipeName)) {
            menuIcon.setIcon(setIconOnSelect(menuIcon));
        } else {
            //
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String fav = getDefaultSharedPreferences(this).getString(getString(R.string.pref_default_key),
                getString(R.string.value));
        if (fav.equals(recipeName)) {
            menu.findItem(R.id.fav).setIcon(setIconOnSelect(menu.findItem(R.id.fav)));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabletSize = getResources().getBoolean(isTablet);

        Intent intent = getIntent();

        invalidateOptionsMenu();

        if(savedInstanceState == null) {

            recipeName = intent.getStringExtra(getString(R.string.name));

            Recipe getRecipe = intent.getParcelableExtra(getString(R.string.recipe_list));

            steps = DataUtils.extractStepsFromJson(getRecipe.getmSteps());

        }else {
            recipeName = savedInstanceState.getString(getString(R.string.name));

            steps = savedInstanceState.getParcelableArrayList(getString(R.string.steps));
        }

        getSupportActionBar().setTitle(recipeName);

        DetailActivityFragment detailActivityFragment = new DetailActivityFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

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
            ArrayList<Ingredient> toDetail = DataUtils.extractIngredientsFromJson(getRecipe.getmIngredients());
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
                intent.putExtra(getString(R.string.position),position);
                startActivity(intent);

        }
    }

    @Override
    public void onMediaPlayerInteraction(Uri uri) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
    }
}
