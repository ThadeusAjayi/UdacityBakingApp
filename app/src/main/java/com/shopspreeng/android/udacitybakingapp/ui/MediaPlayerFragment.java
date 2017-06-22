package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.shopspreeng.android.udacitybakingapp.R;
import com.shopspreeng.android.udacitybakingapp.data.Step;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MediaPlayerFragment.OnMediaPlayerFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link MediaPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaPlayerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnMediaPlayerFragmentInteraction mListener;

    SimpleExoPlayerView mPlayerView;

    Button prev, next;

    TextView descView;

    private ArrayList<Step> description;

    int position;

    private String videoUrl;

    private String tvDescription;

    public MediaPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MediaPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaPlayerFragment newInstance(String param1, String param2) {
        MediaPlayerFragment fragment = new MediaPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);

        descView = (TextView) rootView.findViewById(R.id.step_description);

        descView.setText(tvDescription);

        prev = (Button) rootView.findViewById(R.id.prev);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position > 1) {
                    position--;
                    Log.v("position to previous", " "+position);
                    String prevDesc = description.get(position).getDesc().toString();
                    descView.setText(prevDesc);
                }else {
                    Toast.makeText(getContext(), "Click next", Toast.LENGTH_SHORT).show();
                }

            }
        });

        next = (Button) rootView.findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position < description.size() - 1) {
                    ++position;
                    Log.v("position to previous", " "+position);
                    String nextDesc = description.get(position).getDesc();
                    descView.setText(nextDesc);
                }else {
                    Toast.makeText(getContext(), "Click prev", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMediaPlayerInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMediaPlayerFragmentInteraction) {
            mListener = (OnMediaPlayerFragmentInteraction) context;
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
    public interface OnMediaPlayerFragmentInteraction {
        // TODO: Update argument type and name
        void onMediaPlayerInteraction(Uri uri);
    }
    public void setSteps(ArrayList<Step> desc){
        description = desc;
        Log.v("Step Size", description.size() + " " + description.toString());
    }

    public void setPosition(int pos){
        position = pos;
        Log.v("Position", " "+position);
    }

    public void setText(String desc){
        tvDescription = desc;
    }

    public void setVideoUrl(String url){
        videoUrl = url;
    }
}
