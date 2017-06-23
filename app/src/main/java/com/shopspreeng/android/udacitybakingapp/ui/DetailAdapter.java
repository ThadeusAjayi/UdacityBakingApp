package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Ingredient;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by jayson surface on 19/06/2017.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

    ArrayList<Step> steps = new ArrayList<>();
    LayoutInflater inflater;
    ItemClickListener mClickListener;

    public DetailAdapter(){}

    public DetailAdapter(Context context,@Nullable ArrayList<Step> steps){
        inflater = LayoutInflater.from(context);
        this.steps = steps;
    }

    public void setSteps(ArrayList<Step> steps){
        this.steps = steps;
        notifyDataSetChanged();
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.recipe_card,parent,false);

        DetailViewHolder holder = new DetailViewHolder(rootView);

        return holder;
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {

        ViewGroup.LayoutParams params = holder.cardView.getLayoutParams();


        if(this.steps != null) {
            params.height = 200;
            holder.cardView.setLayoutParams(params);
            Step step = steps.get(position);
            if (step == null) {
                holder.detailText.setText("Ingredients");
            } else {
                holder.detailText.setText(step.getShortDesc().toString());
            }
        }else {
            params.height = WRAP_CONTENT;
            holder.cardView.setLayoutParams(params);
            holder.detailText.setText(R.string.select_recipe);
        }

    }

    @Override
    public int getItemCount() {
        if(steps != null) {
            return steps.size();
        }
        return 0;
    }


    public class DetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView detailText;
        CardView cardView;

        public DetailViewHolder(View itemView) {
            super(itemView);
            if(steps != null) {
                itemView.setOnClickListener(this);
            }
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            detailText = (TextView) itemView.findViewById(R.id.title_view);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClick(view,getAdapterPosition(),detailText.getText().toString());
        }
    }
    public void setClickListener(ItemClickListener itemClickListener){
        mClickListener = itemClickListener;
    }

    interface ItemClickListener{
        void onItemClick(View view, int position,String recipe);
    }
}
