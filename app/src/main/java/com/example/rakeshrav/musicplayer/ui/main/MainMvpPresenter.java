package com.example.rakeshrav.musicplayer.ui.main;

import com.example.rakeshrav.musicplayer.di.PerActivity;
import com.example.rakeshrav.musicplayer.ui.base.MvpPresenter;

@PerActivity
public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {

    void fetchWeatherDataFromPrefs();
    void makeServerCallForecast(int days, String latLng);
}
