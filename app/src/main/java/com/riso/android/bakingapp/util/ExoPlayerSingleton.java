package com.riso.android.bakingapp.util;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by richard.janitor on 17-Jan-18.
 */

public class ExoPlayerSingleton {

    private static ExoPlayerSingleton instance;

    public static ExoPlayerSingleton getInstance(){
        if (instance==null){
            instance = new ExoPlayerSingleton();
        }
        return instance;
    }

    private SimpleExoPlayer exoPlayer;
    private Uri playerUri;
    private boolean playing = true;
    private long exoCurrentposition;

    public void prepareExoPlayer(Context context, Uri uri, SimpleExoPlayerView exoPlayerView) {
        if (context != null && exoPlayerView != null) {
            if (!uri.equals(playerUri) || exoPlayer == null) {
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
                exoPlayerView.setPlayer(exoPlayer);
                playerUri = uri;
                String userAgent = Util.getUserAgent(context, "BakingApp");
                MediaSource mediaSource = new ExtractorMediaSource(playerUri, new DefaultDataSourceFactory(
                        context, userAgent), new DefaultExtractorsFactory(), null, null);
                exoPlayer.prepare(mediaSource);
            }
            exoPlayer.clearVideoSurface();
            exoPlayer.setVideoSurfaceView(
                    (SurfaceView) exoPlayerView.getVideoSurfaceView());
//            exoPlayer.seekTo(exoPlayer.getCurrentPosition() + 1);
            exoCurrentposition = getExoCurretPosition();
            if (exoCurrentposition == 0) {
                exoCurrentposition = exoPlayer.getCurrentPosition() + 1;
            }
            exoPlayer.seekTo(exoCurrentposition);
            exoPlayerView.setPlayer(exoPlayer);
        }
    }

    public void releasePlayer(){
        if(exoPlayer != null)
        {
            exoPlayer.release();
        }
        exoPlayer = null;
    }

    public void goBackground(){
        if(exoPlayer != null){
            playing = exoPlayer.getPlayWhenReady();
            exoPlayer.setPlayWhenReady(false);
        }
    }

    public void goForeground(){
        if(exoPlayer != null){
            exoPlayer.setPlayWhenReady(playing);
        }
    }

    public long exoCurretPosition(){
        return exoCurrentposition;
    }

    public long getExoCurretPosition(){
        return exoPlayer.getCurrentPosition() + 1;
    }

    public void setExoCurrentposition(long exoPosition){
        exoCurrentposition=exoPosition;
    }

}
