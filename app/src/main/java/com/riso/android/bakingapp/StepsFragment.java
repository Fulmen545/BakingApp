package com.riso.android.bakingapp;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.riso.android.bakingapp.data.IngredientItems;
import com.riso.android.bakingapp.data.RecipeColumns;
import com.riso.android.bakingapp.data.StepItems;
import com.riso.android.bakingapp.util.ExoPlayerSingleton;
import com.riso.android.bakingapp.util.IngredientsAdapter;
import com.riso.android.bakingapp.util.StepsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment implements StepsAdapter.ListItemClickListener {
    private static final String POSITION = "position";
    private static final String RECIPE_NAME = "rec_name";
    private static final String RECEPT_POSITION = "recept_position";
    private static final String STEP_COUNT = "step_count";
    private static final String URL = "url";

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private final String STATE_PLAYER_PLAYING = "playerState";

    private int position;
    private String stepPosition;
    private String recept_position;
    private String recipeName;
    private int stepBack;
    private int stepCount;
    @BindView(R.id.back_button)
    TextView back_btn;
    @BindView(R.id.forward_button)
    TextView forward_btn;
    //    @BindView(R.id.steps_rv)
//    RecyclerView stepsRv;
    private StepItems[] mStepArray;
    private StepsAdapter mStepAdapter;

    @BindView(R.id.stepTitle)
    TextView stepTitle_tv;
    @BindView(R.id.stepDesc)
    TextView stepDesc_tv;
    @BindView(R.id.playerView)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.noVideo)
    ImageView noVideoImg;
    SimpleExoPlayer exoPlayer;

    private int mResumeWindow;
    private long mResumePosition;
    private boolean mExoPlayerFullscreen = false;
    private Dialog mFullScreenDialog;
    private boolean mPlayingState;
    private boolean tabletSize;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.app_name);
        ButterKnife.bind(this, view);
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
            mPlayingState = savedInstanceState.getBoolean(STATE_PLAYER_PLAYING);
            ;
        } else {
            mPlayingState = false;
        }
        Bundle bundle = this.getArguments();
        stepCount = bundle.getInt(STEP_COUNT);
        recipeName = bundle.getString(RECIPE_NAME);
        position = bundle.getInt(POSITION, 0);
//        if (tabletSize){
//            stepPosition = Integer.toString(position);
//        } else {
        stepPosition = Integer.toString(position - 1);
//        }
        recept_position = bundle.getString(RECEPT_POSITION);
        if (tabletSize) {
            back_btn.setVisibility(View.GONE);
            forward_btn.setVisibility(View.GONE);
        } else {
            if (position == 1) {
                back_btn.setText(getString(R.string.ingredients));
            } else if (position == 2) {
                back_btn.setText(getString(R.string.rec_introduction));
            } else {
                stepBack = position - 2;
                back_btn.setText(getString(R.string.step_number) + " " + stepBack);
            }
            back_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 1) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(POSITION, position - 1);
                        bundle.putString(RECEPT_POSITION, recept_position);
                        bundle.putString(RECIPE_NAME, recipeName);
                        bundle.putInt(STEP_COUNT, stepCount);
                        DetailFragment df = new DetailFragment();
                        df.setArguments(bundle);
                        changeTo(df, android.R.id.content, "tag1");
//                    Intent intent = new Intent(getActivity(), DetailActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt(POSITION, position - 1);
                        bundle.putString(RECEPT_POSITION, recept_position);
                        bundle.putString(RECIPE_NAME, recipeName);
                        bundle.putInt(STEP_COUNT, stepCount);
                        StepsFragment sf = new StepsFragment();
                        sf.setArguments(bundle);
                        changeTo(sf, android.R.id.content, "tag1");
//                    Intent intent = new Intent(getActivity(), StepActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                    }
                }
            });
            if (stepCount == position + 1) {
                forward_btn.setBackgroundColor(Color.parseColor("#bdbdbd"));
            } else {
                forward_btn.setText(getString(R.string.step_number) + " " + position);
                forward_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ExoPlayerSingleton.getInstance().releasePlayer();
                        Bundle bundle = new Bundle();
                        bundle.putInt(POSITION, position + 1);
                        bundle.putString(RECEPT_POSITION, recept_position);
                        bundle.putString(RECIPE_NAME, recipeName);
                        bundle.putInt(STEP_COUNT, stepCount);
                        StepsFragment sf = new StepsFragment();
                        sf.setArguments(bundle);
                        changeTo(sf, android.R.id.content, "tag1");
//                    Intent intent = new Intent(getActivity(), StepActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                    }
                });
            }
        }
        getActivity().setTitle(recipeName);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//        stepsRv.setLayoutManager(layoutManager);
        getStepArray();
//        mStepAdapter = new StepsAdapter(mStepArray, StepsFragment.this);
//        stepsRv.setAdapter(mStepAdapter);

        //=================================================================
        initExoPlayer();

        stepTitle_tv.setText(mStepArray[0].stepTitle);
        stepDesc_tv.setText(mStepArray[0].description);


    }

    public void initExoPlayer() {
        if (mStepArray[0].url.isEmpty()) {
            exoPlayerView.setVisibility(View.GONE);
        } else {
            if (isOnline()) {
//            try {
//                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//                LoadControl loadControl = new DefaultLoadControl();
//                exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
                Uri videoUri = Uri.parse(mStepArray[0].url);
//                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
//                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//                MediaSource mediaSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);
//                exoPlayerView.setPlayer(exoPlayer);
//                boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
//
//                if (haveResumePosition) {
//                    exoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
//                }
//                exoPlayer.prepare(mediaSource);
//                exoPlayer.setPlayWhenReady(mPlayingState);
//            } catch (Exception e) {
//                Log.e("StepFragment", "RISO exoplayer: " + e.toString());
//            }
//            ExoPlayerSingleton.getInstance().prepareExoPlayer(getContext(), videoUri, exoPlayerView);
//            ExoPlayerSingleton.getInstance().goForeground();
            } else {
                exoPlayerView.setVisibility(View.GONE);
                noVideoImg.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        outState.putBoolean(STATE_PLAYER_PLAYING, mPlayingState);

        super.onSaveInstanceState(outState);
    }

//    private void changeTo(Fragment fragment, int containerViewId) {
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        fragmentManager.beginTransaction().replace(containerViewId, fragment).addToBackStack("tag").commit();
////        getActivity().getSupportFragmentManager().beginTransaction().remove(StepsFragment.this).commit();
//    }

    public void changeTo(Fragment fragment, int containerViewId, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(containerViewId, fragment, tag == null ? fragment.getClass().getName() : tag).commit();

    }


    private void getStepArray() {
        Cursor c = getActivity().getContentResolver().query(RecipeColumns.RecipeEntry.CONTENT_URI_STEPS,
                new String[]{RecipeColumns.RecipeEntry.STEP_TITLE, RecipeColumns.RecipeEntry.DESCRIPTION, RecipeColumns.RecipeEntry.URL},
                RecipeColumns.RecipeEntry.RECIPE_ID + "=? AND " + RecipeColumns.RecipeEntry.STEP_ID + "=?",
                new String[]{recept_position, stepPosition},
                RecipeColumns.RecipeEntry._ID);
        if (c.getCount() != 0) {
            StepItems item;
            mStepArray = new StepItems[c.getCount()];
            int i = 0;
            if (c.moveToFirst()) {
                do {
                    String cTitle = c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.STEP_TITLE));
                    String cDescription = c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.DESCRIPTION));
                    String cUrl = c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.URL));
                    item = new StepItems(cTitle, cDescription, cUrl);
                    mStepArray[i] = item;
                    i++;
                } while (c.moveToNext());
            }
        }
    }

    @Override
    public void onListItemClick(int listItem) {

    }


    @Override
    public void onPause() {
        super.onPause();
        ExoPlayerSingleton.getInstance().goBackground();

//        if (exoPlayerView != null && exoPlayerView.getPlayer() != null) {
//            mResumeWindow = exoPlayerView.getPlayer().getCurrentWindowIndex();
//            mResumePosition = Math.max(0, exoPlayerView.getPlayer().getCurrentPosition());
//
//            mPlayingState = exoPlayer.getPlayWhenReady();
//            exoPlayerView.getPlayer().release();
//        }
//
//        if (mFullScreenDialog != null)
//            mFullScreenDialog.dismiss();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (mStepArray[0].url.isEmpty()) {
            return;
        }

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !tabletSize && isOnline()) {
            Intent intent = new Intent(getContext(), FullScreenVideoActivity.class);
            intent.putExtra(URL, mStepArray[0].url);
            intent.putExtra(STATE_RESUME_POSITION, Math.max(0, exoPlayerView.getPlayer().getCurrentPosition()));
            intent.putExtra(STATE_RESUME_WINDOW, exoPlayerView.getPlayer().getCurrentWindowIndex());
            this.startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ExoPlayerSingleton.getInstance().releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (exoPlayerView != null) {
            ExoPlayerSingleton.getInstance().prepareExoPlayer(getContext(),
                    Uri.parse(mStepArray[0].url), exoPlayerView);
            ExoPlayerSingleton.getInstance().goForeground();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
