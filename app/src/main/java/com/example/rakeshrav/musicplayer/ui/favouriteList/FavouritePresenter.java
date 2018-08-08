package com.example.rakeshrav.musicplayer.ui.favouriteList;

import com.example.rakeshrav.musicplayer.data.DataManager;
import com.example.rakeshrav.musicplayer.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class FavouritePresenter<V extends FavouriteView> extends BasePresenter<V> implements FavouriteMvpPresenter<V> {


    private static final String TAG = FavouritePresenter.class.getSimpleName();

    @Inject
    public FavouritePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

}
