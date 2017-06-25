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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.shopspreeng.android.udacitybakingapp.R.string.steps;

/**
 * Created by jayson surface on 19/06/2017.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

    ArrayList<Step> steps = new ArrayList<>();
    ArrayList<Ingredient> ingred = new ArrayList<>();
    LayoutInflater inflater;
    ItemClickListener mClickListener;

    public DetailAdapter(){}

    public DetailAdapter(Context context,ArrayList<Step> steps){
        inflater = LayoutInflater.from(context);
        this.steps = steps;
    }

    public void setSteps(ArrayList<Step> steps){
        this.steps = steps;
        notifyDataSetChanged();
    }

    public void setIngred(ArrayList<Ingredient> ingred){
        this.ingred = ingred;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.step_card,parent,false);

        DetailViewHolder holder = new DetailViewHolder(rootView);

        return holder;
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {

        ViewGroup.LayoutParams tvParams = holder.detailText.getLayoutParams();
        ViewGroup.LayoutParams cvParams = holder.cardView.getLayoutParams();

        //TODO handle ingredient to be a tv or coordinator layout

            Step step = steps.get(position);
            if (step == null) {
                cvParams.height = WRAP_CONTENT;
                tvParams.height = WRAP_CONTENT;
                holder.cardView.setLayoutParams(cvParams);
                holder.detailText.setLayoutParams(tvParams);
                holder.detailText.setText(ingred.toString());
            } else {
                cvParams.height = 200;
                holder.detailText.setText(step.getShortDesc().toString());
                holder.serial.setText(String.valueOf(position)+".");
            }

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }


    public class DetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView detailText;
        CardView cardView;
        TextView serial;

        public DetailViewHolder(View itemView) {
            super(itemView);
            if(steps != null) {
                itemView.setOnClickListener(this);
            }
            serial = (TextView) itemView.findViewById(R.id.id_tv);
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
