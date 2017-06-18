package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.NetworkUtils;
import com.shopspreeng.android.udacitybakingapp.data.Recipe;
import com.shopspreeng.android.udacitybakingapp.data.RecipeObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class RecipeCardFragment extends Fragment implements ReciperAdapter.ItemClickListener{

    OnRecipeClickListener mListener;

    private RecyclerView mRecyclerView;

    private ReciperAdapter mAdapter;

    public RecipeCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_recipe_card, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_recycler);

        mAdapter = new ReciperAdapter(getActivity(),new HashMap<String,ArrayList<RecipeObject>>());

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
    public void onItemClick(View view, int position) {

        mListener.onRecipeClick(view,position);

        Intent intent = new Intent(getActivity(),DetailActivity.class);
        String key = mAdapter.getItemPosition(position);


        //startActivity(intent);
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
        // TODO: Update argument type and name
        void onRecipeClick(View view,int position);
    }

    private class RecipeAsync extends AsyncTask<Void,Void,Map<String,ArrayList<RecipeObject>>> {

        @Override
        protected Map<String, ArrayList<RecipeObject>> doInBackground(Void... voids) {
            ArrayList<RecipeObject> recipeResult;
            Map<String,ArrayList<RecipeObject>> allList = new HashMap<>();
            try {
                allList = NetworkUtils.extractFromJson(run(NetworkUtils.buildBaseUrl().toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return allList;
        }

        @Override
        protected void onPostExecute(Map<String,ArrayList<RecipeObject>> map) {
            Log.v("MAP SIZE", map.size()+"");
            for(String keys:map.keySet()){
                Log.v(keys, keys);
            }
            mAdapter.setRecipe(map);

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
