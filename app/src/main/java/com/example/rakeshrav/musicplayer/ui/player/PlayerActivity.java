package com.example.rakeshrav.musicplayer.ui.player;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.rakeshrav.musicplayer.R;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.Result;
import com.example.rakeshrav.musicplayer.ui.base.BaseActivity;
import com.example.rakeshrav.musicplayer.ui.favouriteList.FavouriteActivity;
import com.example.rakeshrav.musicplayer.utility.CommonUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerActivity extends BaseActivity implements PlayerView {

    private static final String TAG = PlayerActivity.class.getSimpleName();

    public static final String RESULT = "currentResult";
    @BindView(R.id.ivAction)
    ImageView ivAction;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivFav)
    ImageView ivFav;
    @BindView(R.id.ivCover)
    ImageView ivCover;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.tvSongName)
    TextView tvSongName;
    @BindView(R.id.tvArtistName)
    TextView tvArtistName;
    @BindView(R.id.tvAlbumName)
    TextView tvAlbumName;
    @BindView(R.id.ivList)
    ImageView ivList;
    @BindView(R.id.ivPlayer)
    ImageView ivPlayer;
    @BindView(R.id.ivFavourite)
    ImageView ivFavourite;

//    private String songUrl = "https://audio-ssl.itunes.apple.com/apple-assets-us-std-000001/Music69/v4/e4/6a/bd/e46abd0d-0732-ae08-e89f-6c93a18dacc0/mzaf_1827991663826163479.plus.aac.p.m4a";

    @Inject
    PlayerMvpPresenter<PlayerView> mPresenter;

    private MediaPlayer mMediaPlayer;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, PlayerActivity.class);
        return intent;
    }

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.player_activity);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        mMediaPlayer = new MediaPlayer();

        intent = getIntent();

        setUp();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d(TAG,"New Intent Received");

        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }

        this.intent = intent;
        setUp();
    }

    Result result;
    @Override
    protected void setUp() {

        String resultJson = intent.getStringExtra(RESULT);

        result = new Gson().fromJson(resultJson, Result.class);

        if (result.getArtworkUrl100().contains("jpg") || result.getArtworkUrl100().contains("jpeg")){

            if (result.getArtworkUrl100().contains("100x100")){
                String url = result.getArtworkUrl100().replace("100x100","300x300");
                Picasso.with(this).load(url).fit().into(ivCover);
            }else {
                Picasso.with(this).load(result.getArtworkUrl100()).fit().into(ivCover);
            }
        }else {
            Picasso.with(this).load(result.getArtworkUrl100()).fit().into(ivCover);
        }

        tvAlbumName.setText(result.getCollectionName());
        tvArtistName.setText(result.getArtistName());
        tvSongName.setText(result.getTrackName());

        if (result.isFav()){
         ivFavourite.setImageResource(R.drawable.shape_heart_red);
        }else {
            ivFavourite.setImageResource(R.drawable.shape_heart);
        }

        setmMediaPlayerWithIntenet();
    }

    private void setmMediaPlayerWithIntenet() {
        if (isNetworkConnected()){
            setupMediaPlayer();
        }else {
            showErrorDialog("No Internet Connection Available!", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissErrDialog();
                    setmMediaPlayerWithIntenet();
                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
        boolean isFAv = mPresenter.checkSongIsFav(result);

        if (isFAv){
            ivFavourite.setImageResource(R.drawable.shape_heart_red);
        }else {
            ivFavourite.setImageResource(R.drawable.shape_heart);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setupMediaPlayer() {
        Log.d(TAG, "Setting Media Player");

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mMediaPlayer.setDataSource(result.getPreviewUrl());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.d(TAG, "excep :  " + e.getMessage());
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "media player prepared");
                seekBar.setMax(100);
                seekBar.setProgress(0);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mHandler.removeCallbacks(mUpdateTimeTask);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        stopTrackingTouch(seekBar);
                    }
                });

                updateProgressBar();
                toggleButtons();
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "Song  completed");
                ivPlayer.setImageResource(R.drawable.triangle);
            }
        });
    }

    private void toggleButtons() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            ivPlayer.setImageResource(R.drawable.triangle);
        } else {
            mMediaPlayer.start();
            ivPlayer.setImageResource(R.drawable.combined_shape_2);
        }
    }


    private Handler mHandler = new Handler();;
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mMediaPlayer.getDuration();
            long currentDuration = mMediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            tvEndTime.setText(""+ CommonUtils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            tvStartTime.setText(""+CommonUtils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(CommonUtils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            seekBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    /**
     * When user stops moving the progress hanlder
     * */
    public void stopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mMediaPlayer.getDuration();
        int currentPosition = CommonUtils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mMediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    @OnClick({R.id.ivAction, R.id.ivFav, R.id.ivList, R.id.rlPlayer, R.id.ivFavourite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivAction:
                finish();
                break;
            case R.id.ivFav:
                Intent intent = FavouriteActivity.getStartIntent(this);
                startActivity(intent);
                break;
            case R.id.ivList:
                finish();
                break;
            case R.id.rlPlayer:
                toggleButtons();
                break;
            case R.id.ivFavourite:
                toggleImageView();
                break;
        }
    }

    private void toggleImageView() {
        Log.d(TAG,"togggle results");
        if (result.isFav()){
            Log.d(TAG,"removing favs");
            result.setFav(false);
            ivFavourite.setImageResource(R.drawable.shape_heart);
            mPresenter.removeFavSongs(result);
        }else {
            Log.d(TAG,"Adding favs");
            result.setFav(true);
            ivFavourite.setImageResource(R.drawable.shape_heart_red);
            mPresenter.setFavSongs(result);
        }
    }

    @Override
    protected void onDestroy() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mHandler.removeCallbacks(mUpdateTimeTask);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mMediaPlayer != null){
//            mMediaPlayer.start();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mMediaPlayer != null){
//            if (mMediaPlayer.isPlaying()){
//                mMediaPlayer.pause();
//            }
//        }
    }
}
