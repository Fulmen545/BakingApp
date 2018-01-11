package com.riso.android.bakingapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExoTestActivity extends AppCompatActivity {
    @BindView(R.id.playerView)
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    private static final String TAG = "ExoTEST";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private long mResumePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_test);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
        }
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            LoadControl loadControl = new DefaultLoadControl();
                exoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector, loadControl);
            Uri videoUri = Uri.parse("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4");
//            Uri videoUri = Uri.parse("https://assets-drezy-cdn.rshop.sk//blanco/img/logos/logo.png");
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);
                exoPlayerView.setPlayer(exoPlayer);
            exoPlayerView.getPlayer().seekTo(mResumePosition);

            exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(false);
        } catch (Exception e){
            Log.e(TAG, "RISO exoplayer: " + e.toString());
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (exoPlayerView != null && exoPlayerView.getPlayer() != null) {
//            mResumeWindow = exoPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, exoPlayerView.getPlayer().getCurrentPosition());

            exoPlayerView.getPlayer().release();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

//        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
//        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }

}
