package com.example.rakeshrav.musicplayer.ui.player;

import com.example.rakeshrav.musicplayer.data.network.model.itunesData.Result;
import com.example.rakeshrav.musicplayer.di.PerActivity;
import com.example.rakeshrav.musicplayer.ui.base.MvpPresenter;

@PerActivity
public interface PlayerMvpPresenter<V extends PlayerView> extends MvpPresenter<V> {
    void setFavSongs(Result result);
    void removeFavSongs(Result result);
}
