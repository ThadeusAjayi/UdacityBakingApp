package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.NetworkUtils;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.Inflater;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends AppCompatActivity implements
        RecipeCardFragment.OnRecipeClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRecipeClick(View view, int position) {

    }
}
