package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;
import com.shopspreeng.android.udacitybakingapp.data.RecipeObject;

import java.util.ArrayList;

import static com.shopspreeng.android.udacitybakingapp.R.string.ingredients;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        ArrayList<RecipeObject> objects = intent.getParcelableArrayListExtra(getString(R.string.name));

        TextView detailText = (TextView) findViewById(R.id.detail_text);





 /*       String name = objects.get(0).getmName();
        String mDesc = objects.get(0).getmDesc();
        ArrayList<Recipe> ingredients = objects.get(0).getmIngredients();

        detailText.setText(name + " " + mDesc + " " + ingredients.size());
*/

    }
}
