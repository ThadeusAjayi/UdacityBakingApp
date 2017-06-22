package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
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
public class MediaPlayerFragment extends Fragment implements ExoPlayer.EventListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = DetailActivity.class.getSimpleName();

    private OnMediaPlayerFragmentInteraction mListener;

    SimpleExoPlayerView mPlayerView;

    SimpleExoPlayer mExoPlayer;

    Button prev, next;

    TextView descView;

    private ArrayList<Step> description;

    int position;

    private static long playPosition = 0;

    private String videoUrl;

    private String tvDescription;

    private static MediaSessionCompat mediaSession;

    private PlaybackStateCompat.Builder stateBuilder;

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

        initializeMediaSession();

        initializePlayer(Uri.parse(videoUrl));

        prev = (Button) rootView.findViewById(R.id.prev);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position > 1) {
                    position--;
                    Log.v("position to previous", " "+position);
                    String prevDesc = description.get(position).getDesc().toString();
                    descView.setText(prevDesc);
                    initializePlayer(Uri.parse(description.get(position).getVideoUrl()));
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
                    initializePlayer(Uri.parse(description.get(position).getVideoUrl()));
                }else {
                    Toast.makeText(getContext(), "Click prev", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE /*&& !isTablet*/) {
            hideSystemUI();
            /*if(savedInstanceState != null){
                continuePlayPosition(playPosition,true);
            }*/
            mPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            descView.setVisibility(View.GONE);
            prev.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        }

        return rootView;

    }

    private void hideSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
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

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
            playPosition = mExoPlayer.getCurrentPosition();
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    private void continuePlayPosition(long position, boolean playWhenReady) {
        playPosition = position;
        mExoPlayer.seekTo(position);
        mExoPlayer.setPlayWhenReady(playWhenReady);
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
    }

    public void setText(String desc){
        tvDescription = desc;
    }

    public void setVideoUrl(String url){
        videoUrl = url;
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getContext(), "MediaPlayerFragment");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        //releasePlayer();
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), TAG);
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MySessionCallback());
        mediaSession.setActive(true);
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            continuePlayPosition(0, false);
        }

        @Override
        public void onSkipToNext() {
            //super.onSkipToNext();
        }


    }
}
