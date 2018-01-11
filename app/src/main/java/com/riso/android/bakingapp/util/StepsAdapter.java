package com.riso.android.bakingapp.util;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
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
import com.riso.android.bakingapp.R;
import com.riso.android.bakingapp.data.IngredientItems;
import com.riso.android.bakingapp.data.StepItems;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by richard.janitor on 30-Dec-17.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    private static final String TAG = RecipeAdapter.class.getSimpleName();
    final private StepsAdapter.ListItemClickListener mOnClickListener;

    private StepItems[] mStepItems;

    public StepsAdapter(StepItems[] stepItemses, ListItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        this.mStepItems = stepItemses;
    }


    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.step_card;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        StepsViewHolder viewHolder = new StepsViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + mStepItems.length);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mStepItems.length;
    }

    public interface ListItemClickListener {
        void onListItemClick(int listItem);

    }

    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.stepTitle)
        TextView stepTitle_tv;
        @BindView(R.id.stepDesc)
        TextView stepDesc_tv;
        @BindView(R.id.playerView)
        SimpleExoPlayerView exoPlayerView;
        SimpleExoPlayer exoPlayer;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            if (mStepItems[position].url.isEmpty())
                exoPlayerView.setVisibility(View.GONE);
            else {
                try {
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                    LoadControl loadControl = new DefaultLoadControl();
                    exoPlayer = ExoPlayerFactory.newSimpleInstance(itemView.getContext(), trackSelector, loadControl);
                    Uri videoUri = Uri.parse(mStepItems[position].url);
                    DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                    MediaSource mediaSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);
                    exoPlayerView.setPlayer(exoPlayer);
                    exoPlayer.prepare(mediaSource);
                    exoPlayer.setPlayWhenReady(false);
                } catch (Exception e) {
                    Log.e(TAG, "RISO exoplayer: " + e.toString());
                }
            }
            stepTitle_tv.setText(mStepItems[position].stepTitle);
            stepDesc_tv.setText(mStepItems[position].description);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
