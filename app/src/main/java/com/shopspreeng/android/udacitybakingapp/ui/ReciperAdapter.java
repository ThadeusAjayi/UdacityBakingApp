package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;

import java.util.ArrayList;


/**
 * Created by jayson surface on 14/06/2017.
 */

public class ReciperAdapter extends RecyclerView.Adapter<ReciperAdapter.RecipeViewHolder> {

    LayoutInflater inflater;
    ItemClickListener mClickListener;
    ArrayList<Recipe> mRecipe;

    public ReciperAdapter(Context context, ArrayList<Recipe> recipe){
        inflater = LayoutInflater.from(context);
        mRecipe = recipe;
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
        String currentRecipe = mRecipe.get(position).getmName();
        holder.titleHead.setText(currentRecipe);
    }

    @Override
    public int getItemCount() {
        return mRecipe.size();
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleHead;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleHead = (TextView) itemView.findViewById(R.id.title_view);
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
