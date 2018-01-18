package com.riso.android.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.exoplayer2.C;
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
import com.riso.android.bakingapp.util.ExoPlayerSingleton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullScreenVideoActivity extends Activity {
    private static final String URL = "url";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_RESUME_WINDOW = "resumeWindow";


    @BindView(R.id.playerView)
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    private String url;
    private long resumePosition;
    private int mResumeWindow;
    private boolean killVideo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_screen_video);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra(URL);
            resumePosition = intent.getLongExtra(STATE_RESUME_POSITION,0);
            mResumeWindow = intent.getIntExtra(STATE_RESUME_WINDOW, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        try {
//            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//            LoadControl loadControl = new DefaultLoadControl();
//            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
//            Uri videoUri = Uri.parse(url);
//            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
//            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//            MediaSource mediaSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);
//            exoPlayerView.setPlayer(exoPlayer);
////            boolean haveResumePosition = resumePosition != C.INDEX_UNSET;
//
////            if (haveResumePosition) {
//                exoPlayerView.getPlayer().seekTo(mResumeWindow, resumePosition);
////            }
//            exoPlayer.prepare(mediaSource);
//            exoPlayer.setPlayWhenReady(true);
//        } catch (Exception e) {
//            Log.e("StepFragment", "RISO exoplayer: " + e.toString());
//        }

        ExoPlayerSingleton.getInstance()
                .prepareExoPlayer(getApplicationContext(),
                        Uri.parse(url), exoPlayerView);
        ExoPlayerSingleton.getInstance().goForeground();
    }

    @Override
    public void onBackPressed(){
        killVideo = false;
        super.onBackPressed();
    }

//    @Override
protected void onPause(){
    super.onPause();
    ExoPlayerSingleton.getInstance().goBackground();
}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (killVideo) {
            ExoPlayerSingleton.getInstance().releasePlayer();
        }
    }
}
