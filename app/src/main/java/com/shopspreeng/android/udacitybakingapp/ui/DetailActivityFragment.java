package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Ingredient;
import com.shopspreeng.android.udacitybakingapp.data.NetworkUtils;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.shopspreeng.android.udacitybakingapp.R.string.steps;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailActivityFragment.OnFragmentInteractionListener} interface
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

    String recipeName;


    private OnFragmentInteractionListener mListener;

    public DetailActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailActivityFragment newInstance(String param1, String param2) {
        DetailActivityFragment fragment = new DetailActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeName = getActivity().getIntent().getExtras().get(getString(R.string.name)).toString();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.detail_recycler);

        mDetailAdapter = new DetailAdapter(getContext(), new ArrayList<Step>(), null);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        mRecycler.setAdapter(mDetailAdapter);

        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        ArrayList<Step> steps = b.getParcelableArrayList(getString(R.string.steps));

        mDetailAdapter.setSteps(steps);

        mDetailAdapter.setClickListener(this);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        mListener.onFragmentInteraction(view,position,recipe);
        if(position == 0) {
            new AsyncTask<Void, Void, ArrayList<Ingredient>>() {
                @Override
                protected ArrayList<Ingredient> doInBackground(Void... voids) {

                    ArrayList<Ingredient> result = new ArrayList<>();
                    try {
                        result = NetworkUtils.extractIngredientsFromJson(run(NetworkUtils.buildBaseUrl().toString()), recipeName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return result;
                }

                @Override
                protected void onPostExecute(ArrayList<Ingredient> ingredient) {
                    super.onPostExecute(ingredient);
                    mRecycler.setVisibility(View.GONE);

                    IngredientFragment ingredientFragment = new IngredientFragment();
                    ingredientFragment.setIngredients(ingredient);

                    getChildFragmentManager().beginTransaction()
                            .add(R.id.detail_container,ingredientFragment)
                            .commit();

                }
            }.execute();
        }else {
            mRecycler.setVisibility(View.GONE);

            MediaPlayerFragment mediaFragment = new MediaPlayerFragment();
            //mediaFragment.setDescView(description);
            mediaFragment.setPosition(position);
            mediaFragment.setText(mDetailAdapter.steps.get(position).getDesc());
            mediaFragment.setSteps(mDetailAdapter.steps);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, mediaFragment)
                    .commit();

        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(View view, int position, String recipe);
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
