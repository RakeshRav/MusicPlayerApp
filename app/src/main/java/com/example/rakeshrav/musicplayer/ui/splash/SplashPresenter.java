package com.example.rakeshrav.musicplayer.ui.splash;

import android.util.Log;

import com.example.rakeshrav.musicplayer.data.DataManager;
import com.example.rakeshrav.musicplayer.data.network.RestClient;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.ItunesData;
import com.example.rakeshrav.musicplayer.ui.base.BasePresenter;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SplashPresenter<V extends SplashView> extends BasePresenter<V> implements SplashMvpPresenter<V> {


    private static final String TAG = SplashPresenter.class.getSimpleName();

    @Inject
    public SplashPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void getSongList(String term, String limit) {
        RestClient.getApiServicePojo().getSongsList(term, String.valueOf(limit),
                "music",
                new Callback<ItunesData>() {
                    @Override
                    public void success(ItunesData itunesData, Response response) {
                        Log.d(TAG,"Success songs list : "+new Gson().toJson(itunesData));
                        getMvpView().populateData(itunesData);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG,"failure songs list : "+error.toString());
                    }
                });
    }
}
