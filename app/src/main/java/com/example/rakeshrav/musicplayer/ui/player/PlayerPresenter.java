package com.example.rakeshrav.musicplayer.ui.player;

import com.example.rakeshrav.musicplayer.data.DataManager;
import com.example.rakeshrav.musicplayer.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class PlayerPresenter<V extends PlayerView> extends BasePresenter<V> implements PlayerMvpPresenter<V> {


    private static final String TAG = PlayerPresenter.class.getSimpleName();

    @Inject
    public PlayerPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

}
