package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Ingredient;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IngredientFragment.OnIngredientInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class IngredientFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnIngredientInteractionListener mListener;

    private TextView ingredientTv;

    private ArrayList<Ingredient> ingredients;

    ArrayList<Ingredient> allIngred;


    public IngredientFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ingredient, container, false);

        ingredientTv = (TextView) rootView.findViewById(R.id.ingredient_tv);

        if(savedInstanceState != null){
            ArrayList<Ingredient> savedIngred = savedInstanceState.
                    getParcelableArrayList(getString(R.string.ingredients));
            setIngredients(savedIngred);
        }

        allIngred = getIngredients();

        mListener.onIngredientInteraction(allIngred);

        int count = 1;
        for(Ingredient val : allIngred){
            ingredientTv.append(String.valueOf(count) + ":\t" +val.describeContents() + "\n \t" + val.getmQty() + " " + val.getmMeasure());
            count++;
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.ingredients),allIngred);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIngredientInteractionListener) {
            mListener = (OnIngredientInteractionListener) context;
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnIngredientInteractionListener {
        // TODO: Update argument type and name
        void onIngredientInteraction(ArrayList<Ingredient> ingredients);
    }

    public ArrayList<Ingredient> getIngredients(){
        return this.ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients){
        this.ingredients = ingredients;
    }
}
