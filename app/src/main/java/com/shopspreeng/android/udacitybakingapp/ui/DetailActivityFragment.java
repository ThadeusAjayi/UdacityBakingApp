package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.DataUtils;
import com.shopspreeng.android.udacitybakingapp.data.Ingredient;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.util.ArrayList;

import static com.shopspreeng.android.udacitybakingapp.data.DataUtils.ingredientToString;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailActivityFragment.OnStepInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailActivityFragment#} factory method to
 * create an instance of this fragment.
 */
public class DetailActivityFragment extends Fragment implements DetailAdapter.ItemClickListener {

    private RecyclerView mRecycler;

    DetailAdapter mDetailAdapter;

    ArrayList<Ingredient> ingredients;

    ArrayList<Step> stepList;

    private OnStepInteractionListener mListener;

    private boolean tabletSize;

    TextView ingredTv;

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

        ingredTv = (TextView) view.findViewById(R.id.ingredient_tv);

        tabletSize = getContext().getResources().getBoolean(R.bool.isTablet);

        mDetailAdapter = new DetailAdapter(getContext(), new ArrayList<Step>());

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        mRecycler.setAdapter(mDetailAdapter);

        if(savedInstanceState != null){
            stepList = savedInstanceState.getParcelableArrayList(getString(R.string.steps));
            ingredients = savedInstanceState.getParcelableArrayList(getString(R.string.ingredients));
            mDetailAdapter.setSteps(stepList);
            ingredTv.setText("Ingredients: \n \n" +ingredientToString(ingredients));
        }else {
            if(tabletSize) {
                Recipe getRecipe = getActivity().getIntent().getParcelableExtra(getString(R.string.recipe_list));
                stepList = DataUtils.extractStepsFromJson(getRecipe.getmSteps());
                ingredients = DataUtils.extractIngredientsFromJson(getRecipe.getmIngredients());
            }else {
                stepList = getSteps();
                ingredients = getIngredients();
            }

            mDetailAdapter.setSteps(stepList);
            ingredTv.setText("Ingredients: \n \n" + ingredientToString(ingredients));

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
