package com.shopspreeng.android.udacitybakingapp.ui;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shopspreeng.android.udacitybakingapp.BakingDatabaseUpdateService;
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
import static com.shopspreeng.android.udacitybakingapp.R.string.ingredients;


public class MainActivity extends AppCompatActivity implements MainRecipeFragment.OnRecipeClickListener,
        MainRecipeAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;

    private MainRecipeAdapter mAdapter;

    ArrayList<Recipe> mRecipeResult = new ArrayList<>();

    boolean tabletSize, isLoading;

    TextView loadingTv, errorView;

    ProgressBar loadingPb;

    Snackbar snackOver;

    ArrayList<Ingredient> ingredients;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabletSize = getResources().getBoolean(isTablet);

        mRecyclerView = (RecyclerView) findViewById(R.id.recipe_recycler);

        loadingPb = (ProgressBar) findViewById(R.id.recipe_progress);

        loadingTv = (TextView) findViewById(R.id.loading_text);

        errorView = (TextView) findViewById(R.id.error_view);

        mAdapter = new MainRecipeAdapter(this, new ArrayList<Recipe>());

        if (tabletSize) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

            mRecyclerView.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            mRecyclerView.setLayoutManager(layoutManager);
        }

        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {

            mRecipeResult = savedInstanceState.getParcelableArrayList(getString(R.string.recipe_list));

            mAdapter.setRecipe(mRecipeResult);

        } else {

            new RecipeAsync().execute();

        }

        mAdapter.setClickListener(this);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mRecipeResult != null) {
            outState.putParcelableArrayList(getString(R.string.recipe_list), mRecipeResult);
        }
    }

    public void repeatError(){
        errorView.setText(getString(R.string.no_internet));
        errorView.setVisibility(View.VISIBLE);
        snackOver.make(errorView,R.string.no_internet, BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!isThereInternetConnection()){
                            repeatError();
                        }else {
                            new RecipeAsync().execute();
                        }
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.highlights))
                .show();
    }

    @Override
    public void onItemClick(View view, int position, final String recipe) {

        if(!isThereInternetConnection()){
            errorView.setVisibility(View.INVISIBLE);
            Snackbar.make(errorView,R.string.no_internet, BaseTransientBottomBar.LENGTH_INDEFINITE).show();
            return;
        }

        if(isLoading){
            return;
        }

        new IngredientAsync().execute(recipe);

    }

    private class IngredientAsync extends AsyncTask<String,Void,ArrayList<Ingredient>>{

        String globalString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            controlViewOnLoad(true);
            errorView.setVisibility(View.VISIBLE);
        }

        @Override
            protected ArrayList<Ingredient> doInBackground(String... strings) {

                globalString = strings[0];
                ArrayList<Ingredient> result = new ArrayList<>();
                try {
                    result = NetworkUtils.extractIngredientsFromJson(run(NetworkUtils.buildBaseUrl().toString()), globalString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<Ingredient> ingredient) {
                super.onPostExecute(ingredient);

                Log.v("Ingredients size", String.valueOf(ingredient.size()));
                if(ingredient == null){
                    errorView.setVisibility(View.INVISIBLE);
                    Snackbar.make(errorView,R.string.error_loading, BaseTransientBottomBar.LENGTH_INDEFINITE).show();
                }else {
                    ingredients = ingredient;
                    new StepAsync().execute(globalString);
                }

            }
    }

    private class StepAsync extends AsyncTask<String,Void,ArrayList<Step>>{

        String globalString;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected ArrayList<Step> doInBackground(String... strings) {

                globalString = strings[0];

                ArrayList<Step> result = new ArrayList<>();
                try {
                    result.add(0,null);
                    result.addAll(NetworkUtils.extractStepsFromJson(run(NetworkUtils.buildBaseUrl().toString()),globalString));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<Step> step) {
                super.onPostExecute(step);
                controlViewOnLoad(false);

                if(step != null) {
                    Log.v("Step size", String.valueOf(step.size()));
                    Intent stepIntent = new Intent(getApplicationContext(), DetailActivity.class);

                    stepIntent.putExtra(getString(R.string.name),globalString);

                    stepIntent.putParcelableArrayListExtra(getString(R.string.steps), step);

                    stepIntent.putParcelableArrayListExtra(getString(R.string.ingredients), ingredients);

                    startActivity(stepIntent);
                }
            }

    }

    @Override
    public void onRecipeClick(View view, int position, String recipe) {
    }

    protected boolean isThereInternetConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private class RecipeAsync extends AsyncTask<Void, Void, ArrayList<Recipe>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            controlViewOnLoad(true);
        }

        @Override
        protected ArrayList<Recipe> doInBackground(Void... voids) {
            ArrayList<Recipe> recipeResult = new ArrayList<>();
            try {
                recipeResult = NetworkUtils.extractRecipeFromJson(run(NetworkUtils.buildBaseUrl().toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return recipeResult;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipeResult) {
            if(recipeResult == null){
                repeatError();
            }else {
                controlViewOnLoad(false);
                mRecipeResult = recipeResult;
                Intent updateDb = new Intent(getApplicationContext(), BakingDatabaseUpdateService.class);
                updateDb.setAction(BakingDatabaseUpdateService.UPDATE_DB);
                updateDb.putParcelableArrayListExtra(getString(R.string.recipe_list),mRecipeResult);
                mAdapter.setRecipe(mRecipeResult);
            }
        }
    }

    public void controlViewOnLoad(boolean showBoolean) {
        if (showBoolean) {
            isLoading = true;
            loadingTv.setVisibility(View.VISIBLE);
            loadingPb.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.VISIBLE);
        } else {
            isLoading = false;
            loadingTv.setVisibility(View.GONE);
            loadingPb.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
        }
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
