package com.example.rakeshrav.musicplayer.ui.splash;

import com.example.rakeshrav.musicplayer.data.network.model.itunesData.ItunesData;
import com.example.rakeshrav.musicplayer.ui.base.MvpView;

public interface SplashView extends MvpView {

    void populateData(ItunesData itunesData);
}
