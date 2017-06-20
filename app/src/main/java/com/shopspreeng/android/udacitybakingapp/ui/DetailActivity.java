package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Ingredient;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements DetailActivityFragment.OnFragmentInteractionListener{

    DetailAdapter detailAdapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String name = b.getString(getString(R.string.name));
        getSupportActionBar().setTitle(name);

        DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.detail_container,detailActivityFragment)
                .commit();



    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
