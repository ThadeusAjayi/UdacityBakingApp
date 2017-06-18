package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;
import com.shopspreeng.android.udacitybakingapp.data.RecipeObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.R.attr.key;

/**
 * Created by jayson surface on 14/06/2017.
 */

public class ReciperAdapter extends RecyclerView.Adapter<ReciperAdapter.RecipeViewHolder> {

    LayoutInflater inflater;
    ArrayList<String> keys = new ArrayList<>();
    ItemClickListener mClickListener;
    Map<String,ArrayList<RecipeObject>> mRecipeMap = new HashMap<>();
    ArrayList<String> position = new ArrayList<>();

    public ReciperAdapter(Context context, Map<String,ArrayList<RecipeObject>> recipe){
        inflater = LayoutInflater.from(context);
        mRecipeMap = recipe;
    }

    public void setRecipe(Map<String,ArrayList<RecipeObject>> recipe){
        mRecipeMap = recipe;
        for(String key: mRecipeMap.keySet()){
            keys.add(key);
        }
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

        holder.titleHead.setText(keys.get(position));

    }

    @Override
    public int getItemCount() {
        return mRecipeMap.size();
    }

    public String getItemPosition(int key){
        return position.get(key);
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
            mClickListener.onItemClick(view,getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener){
        mClickListener = itemClickListener;
    }

    interface ItemClickListener{
        void onItemClick(View view, int position);
    }


}
