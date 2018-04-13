package com.master.exoplayerdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by cenzen on 2018/3/8.
 */

public class ExoPlayerActivity extends AppCompatActivity {

    private static final String URL = "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";

    public static void start(Context context) {
        Intent starter = new Intent(context, ExoPlayerActivity.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            context.startActivity(starter, ActivityOptions.makeSceneTransitionAnimation());
//        }
        context.startActivity(starter);
    }

    private PlayerView mPlayerView;

    private SimpleExoPlayer mExoPlayer;
    private ExoPlayerListener exoPlayerListener;

    private boolean shouldAutoPlay;
    private int resumeWindow;
    private long resumePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shouldAutoPlay = true;
        clearResumePosition();

        setContentView(R.layout.exo_activity_player);

        mPlayerView = findViewById(R.id.player_view);
        mPlayerView.requestFocus();
        exoPlayerListener = new ExoPlayerListener();
    }

    @Override
    public void onNewIntent(Intent intent) {
        releasePlayer();
        shouldAutoPlay = true;
        clearResumePosition();
        setIntent(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
            DefaultTrackSelector selector = new DefaultTrackSelector(new DefaultBandwidthMeter());
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, selector);
            mExoPlayer.addListener(exoPlayerListener);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(shouldAutoPlay);

            //mPlayerView.setPlaybackPreparer(this);
        }

        MediaSource mediaSource = ExoMediaSource.buildMediaSource(Uri.parse(URL));

        boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            mExoPlayer.seekTo(resumeWindow, resumePosition);
        }
        mExoPlayer.prepare(mediaSource, !haveResumePosition, false);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            shouldAutoPlay = mExoPlayer.getPlayWhenReady();
            updateResumePosition();
            //mExoPlayer.removeListener(exoPlayerListener);
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void updateResumePosition() {
        resumeWindow = mExoPlayer.getCurrentWindowIndex();
        resumePosition = Math.max(0, mExoPlayer.getContentPosition());
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }


}
