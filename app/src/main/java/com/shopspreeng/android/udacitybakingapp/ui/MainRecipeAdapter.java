package com.shopspreeng.android.udacitybakingapp.ui;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopspreeng.android.udacitybakingapp.BakingDatabaseUpdateService;
import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.BakingContract;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.R.attr.data;


/**
 * Created by jayson surface on 14/06/2017.
 */

public class MainRecipeAdapter extends RecyclerView.Adapter<MainRecipeAdapter.RecipeViewHolder> {

    LayoutInflater inflater;
    ItemClickListener mClickListener;
    ArrayList<Recipe> mRecipe;
    Context context;

    public MainRecipeAdapter(Context context, ArrayList<Recipe> recipes){
        inflater = LayoutInflater.from(context);
        mRecipe = recipes;
    }

    public void setRecipe(ArrayList<Recipe> recipe){
        mRecipe = recipe;
        notifyDataSetChanged();
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View recipeView = inflater.inflate(R.layout.recipe_card,parent,false);

        RecipeViewHolder holder = new RecipeViewHolder(recipeView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {


        if(context.getResources().getBoolean(R.bool.isTablet)){
            ViewGroup.LayoutParams params = holder.titleHead.getLayoutParams();
        }

        if(mRecipe != null) {
            String recipeName = mRecipe.get(position).getmName();
            holder.titleHead.setText(recipeName);
            Picasso.with(context).load(setImage(recipeName)).fit().into(holder.recipeImage);
        }

    }

    public int setImage(String recipe){
        if(recipe.contains("Yellow")){
            return R.drawable.yellowcake;
        }else if(recipe.contains("Brownies")){
            return R.drawable.brownies;
        }else if(recipe.contains("Nutella")){
            return R.drawable.nutella;
        }else {
            return R.drawable.cheesecake;
        }
    }

    @Override
    public int getItemCount() {
        return mRecipe.size();
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleHead;
        ImageView recipeImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            context = itemView.getContext();
            titleHead = (TextView) itemView.findViewById(R.id.title_view);
            recipeImage =(ImageView) itemView.findViewById(R.id.recipe_image);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(view,getAdapterPosition(),titleHead.getText().toString());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener){
        mClickListener = itemClickListener;
    }

    interface ItemClickListener{
        void onItemClick(View view, int position,String recipe);
    }


}
