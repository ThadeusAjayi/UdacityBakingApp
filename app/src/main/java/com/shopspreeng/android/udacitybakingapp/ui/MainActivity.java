package com.shopspreeng.android.udacitybakingapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        MainRecipeFragment.OnRecipeClickListener {

    public boolean isTablet;

    DetailAdapter mDetailAdapter;

    RecyclerView mRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.recipe_list) != null){
            isTablet = true;

            mRecycler = (RecyclerView) findViewById(R.id.detail_recycler);

            mDetailAdapter = new DetailAdapter(this, new ArrayList<Step>(), null);

            mRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

            mRecycler.setAdapter(mDetailAdapter);
        }else {
            isTablet = false;
        }

    }

    @Override
    public void onRecipeClick(View view, int position,final String recipe) {
        Toast.makeText(getApplicationContext(),"main click" + position + " " + recipe,Toast.LENGTH_SHORT)
                .show();

    }
}
