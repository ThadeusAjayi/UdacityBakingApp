package com.shopspreeng.android.udacitybakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.GridLayoutManager;
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

    private boolean tabletSize;

    public MediaPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(getString(R.string.playPosition),playPosition);
        outState.putString(getString(R.string.videoUrl),videoUrl);
        outState.putInt(getString(R.string.description_position),position);
        outState.putString(getString(R.string.description),tvDescription);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);

        tabletSize = getContext().getResources().getBoolean(R.bool.isTablet);

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);

        descView = (TextView) rootView.findViewById(R.id.step_description);

        if(savedInstanceState != null){
            tvDescription = savedInstanceState.getString(getString(R.string.description));
            videoUrl = savedInstanceState.getString(getString(R.string.videoUrl));
            playPosition = savedInstanceState.getLong(getString(R.string.playPosition));
            position = savedInstanceState.getInt(getString(R.string.description_position));
        }

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

            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !tabletSize){
            /*if(savedInstanceState != null){
                continuePlayPosition(playPosition,true);
            }*/
                hideSystemUI();
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

    public interface OnMediaPlayerFragmentInteraction {
        // TODO: Update argument type and name
        void onMediaPlayerInteraction(Uri uri);
    }
    public void setSteps(ArrayList<Step> desc){
        description = desc;
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
            mExoPlayer.setPlayWhenReady(false);
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
        if(mExoPlayer != null){
            mExoPlayer.release();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mExoPlayer != null) {
            releasePlayer();
        }
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
            super.onSkipToNext();
        }


    }

}
