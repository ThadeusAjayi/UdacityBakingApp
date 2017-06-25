package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Ingredient;
import com.shopspreeng.android.udacitybakingapp.data.NetworkUtils;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailActivityFragment.OnStepInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailActivityFragment extends Fragment implements DetailAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mRecycler;

    DetailAdapter mDetailAdapter;

    ArrayList<Ingredient> ingredients;

    ArrayList<Step> stepList;

    private OnStepInteractionListener mListener;

    private boolean tabletSize;

    public DetailActivityFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.detail_recycler);

        tabletSize = getContext().getResources().getBoolean(R.bool.isTablet);

        mDetailAdapter = new DetailAdapter(getContext(), new ArrayList<Step>());

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        mRecycler.setAdapter(mDetailAdapter);

        if(savedInstanceState != null){
            stepList = savedInstanceState.getParcelableArrayList(getString(R.string.steps));
            ingredients = savedInstanceState.getParcelableArrayList(getString(R.string.ingredients));
            mDetailAdapter.setSteps(stepList);
            mDetailAdapter.setIngred(ingredients);
        }else {
            if(tabletSize) {
                stepList = getActivity().getIntent().getParcelableArrayListExtra(getString(R.string.steps));
                ingredients = getActivity().getIntent().getParcelableArrayListExtra(getString(R.string.ingredients));
            }else {
                stepList = getSteps();
                ingredients = getIngredients();
            }

            mDetailAdapter.setSteps(stepList);
            mDetailAdapter.setIngred(ingredients);

        }

        mDetailAdapter.setClickListener(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.steps),stepList);
        outState.putParcelableArrayList(getString(R.string.ingredients),ingredients);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepInteractionListener) {
            mListener = (OnStepInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(View view, int position, final String recipe) {
        mListener.onStepInteraction(view,position,recipe);

    }

    public interface OnStepInteractionListener {
        // TODO: Update argument type and name
        void onStepInteraction(View view, int position, String recipe);
    }

    public void setStepsList(ArrayList<Step> steps){
        stepList = steps;
    }

    public ArrayList<Step> getSteps(){
        return stepList;
    }

    public void setIngredientList(ArrayList<Ingredient> ingredient){
        ingredients = ingredient;
    }

    public ArrayList<Ingredient> getIngredients(){
        return ingredients;
    }

}
