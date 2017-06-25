package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.util.ArrayList;

import static com.shopspreeng.android.udacitybakingapp.R.string.steps;

public class PhoneMediaPlayerActivity extends AppCompatActivity implements MediaPlayerFragment.OnMediaPlayerFragmentInteraction{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_media_player);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String name = getIntent().getExtras().getString(getString(R.string.name));

        getSupportActionBar().setTitle(name);

        ArrayList<Step> steps = getIntent().getParcelableArrayListExtra(getString(R.string.steps));
        int position = getIntent().getExtras().getInt(getString(R.string.position));

        MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
        mediaPlayerFragment.setPosition(position);
        mediaPlayerFragment.setText(steps.get(position).getDesc());
        mediaPlayerFragment.setSteps(steps);
        mediaPlayerFragment.setVideoUrl(steps.get(position).getVideoUrl());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.media_container,mediaPlayerFragment)
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMediaPlayerInteraction(Uri uri) {

    }
}
