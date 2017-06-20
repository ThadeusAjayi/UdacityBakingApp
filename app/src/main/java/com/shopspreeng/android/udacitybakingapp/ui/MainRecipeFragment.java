package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.NetworkUtils;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class MainRecipeFragment extends Fragment implements MainRecipeAdapter.ItemClickListener {

    OnRecipeClickListener mListener;

    private RecyclerView mRecyclerView;

    private MainRecipeAdapter mAdapter;

    public MainRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_recycler);

        mAdapter = new MainRecipeAdapter(getActivity(), new ArrayList<Recipe>());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setClickListener(this);

        new RecipeAsync().execute();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeClickListener) {
            mListener = (OnRecipeClickListener) context;
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

        mListener.onRecipeClick(view, position,recipe);

        new AsyncTask<Void, Void, ArrayList<Step>>() {
            @Override
            protected ArrayList<Step> doInBackground(Void... voids) {
                ArrayList<Step> result = new ArrayList<>();
                try {
                    result = NetworkUtils.extractStepsFromJson(run(NetworkUtils.buildBaseUrl().toString()),recipe);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<Step> step) {
                super.onPostExecute(step);
                Toast.makeText(getContext(), "fragment click", Toast.LENGTH_SHORT).show();
                Intent detailIntent = new Intent(getActivity(),DetailActivity.class);
                Bundle b = new Bundle();
                b.putString(getString(R.string.name),recipe);
                b.putParcelableArrayList(getString(R.string.steps),step);
                detailIntent.putExtras(b);
                startActivity(detailIntent);
            }
        }.execute();

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

    public interface OnRecipeClickListener {
        void onRecipeClick(View view, int position, String recipe);
    }

    private class RecipeAsync extends AsyncTask<Void, Void, ArrayList<Recipe>> {

        @Override
        protected ArrayList<Recipe> doInBackground(Void... voids) {
            ArrayList<Recipe> recipeResult = new ArrayList<>();
            try {
                recipeResult = NetworkUtils.extractRecipeFromJson(run(NetworkUtils.buildBaseUrl().toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return recipeResult;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipeResult) {
            mAdapter.setRecipe(recipeResult);
        }
    }

    OkHttpClient connect = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = connect.newCall(request).execute();

        String result = response.body().string();

        return result;
    }

}
