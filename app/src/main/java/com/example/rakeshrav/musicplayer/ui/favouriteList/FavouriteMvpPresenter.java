package com.example.rakeshrav.musicplayer.ui.favouriteList;

import com.example.rakeshrav.musicplayer.data.network.model.itunesData.Result;
import com.example.rakeshrav.musicplayer.di.PerActivity;
import com.example.rakeshrav.musicplayer.ui.base.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

@PerActivity
public interface FavouriteMvpPresenter<V extends FavouriteView> extends MvpPresenter<V> {
    List<Result> getFavResults();
    void removeFavSongs(Result result);
}
