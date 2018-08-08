package com.example.rakeshrav.musicplayer.ui.favouriteList;

import android.util.Log;

import com.example.rakeshrav.musicplayer.data.DataManager;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.ItunesData;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.Result;
import com.example.rakeshrav.musicplayer.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class FavouritePresenter<V extends FavouriteView> extends BasePresenter<V> implements FavouriteMvpPresenter<V> {


    private static final String TAG = FavouritePresenter.class.getSimpleName();

    @Inject
    public FavouritePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public List<Result> getFavResults() {

        ItunesData itunesData = getDataManager().getFavsResult();

        if (itunesData == null){
            Log.d(TAG,"Nothing found fav");
            return null;
        }else {
            if (itunesData.getResults() == null){
                Log.d(TAG,"Nothing found fav");
                return null;
            }else {
                Log.d(TAG,"Favs Found");
                return itunesData.getResults();
            }
        }
    }
}
