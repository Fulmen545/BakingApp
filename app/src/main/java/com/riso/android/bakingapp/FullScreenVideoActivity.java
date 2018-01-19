package com.riso.android.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
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
            resumePosition = intent.getLongExtra(STATE_RESUME_POSITION, 0);
            mResumeWindow = intent.getIntExtra(STATE_RESUME_WINDOW, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ExoPlayerSingleton.getInstance()
                .prepareExoPlayer(getApplicationContext(),
                        Uri.parse(url), exoPlayerView);
        ExoPlayerSingleton.getInstance().goForeground();
    }

    @Override
    public void onBackPressed() {
        killVideo = false;
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
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
