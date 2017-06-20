package com.shopspreeng.android.udacitybakingapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.shopspreeng.android.udacitybakingapp.R;

public class MainActivity extends AppCompatActivity implements
        MainRecipeFragment.OnRecipeClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRecipeClick(View view, int position,final String recipe) {
        Toast.makeText(getApplicationContext(),"main click" + position + " " + recipe,Toast.LENGTH_SHORT)
                .show();

    }
}
