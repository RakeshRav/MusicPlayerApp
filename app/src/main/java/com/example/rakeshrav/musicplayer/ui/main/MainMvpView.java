package com.example.rakeshrav.musicplayer.ui.main;

import com.example.rakeshrav.musicplayer.data.network.model.forecastData.ForecastData;
import com.example.rakeshrav.musicplayer.ui.base.MvpView;

public interface MainMvpView extends MvpView{

    void populateDataInUI(ForecastData forecastData);
    void makeServerCall(String latLng);
}
