package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.util.ArrayList;

/**
 * Created by jayson surface on 19/06/2017.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

    ArrayList<Step> steps = new ArrayList<>();
    LayoutInflater inflater;
    private int selectedPosition = 0;

    public DetailAdapter(Context context, ArrayList<Step> steps){
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

        if(selectedPosition == position){
            holder.detailText.setText(R.string.ingredients);
        }else {
            String currentStep = steps.get(position).getShortDesc();
            holder.detailText.setText(currentStep);
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder{

        TextView detailText;

        public DetailViewHolder(View itemView) {
            super(itemView);

            detailText = (TextView) itemView.findViewById(R.id.title_view);
        }
    }
}
