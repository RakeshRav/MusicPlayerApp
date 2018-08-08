package com.example.rakeshrav.musicplayer.ui.main;

import android.util.Log;
import android.view.View;

import com.example.rakeshrav.musicplayer.BuildConfig;
import com.example.rakeshrav.musicplayer.data.DataManager;
import com.example.rakeshrav.musicplayer.data.network.RestClient;
import com.example.rakeshrav.musicplayer.data.network.model.forecastData.ForecastData;
import com.example.rakeshrav.musicplayer.ui.base.BasePresenter;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {


    private static final String TAG = MainPresenter.class.getSimpleName();

    @Inject
    public MainPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void fetchWeatherDataFromPrefs() {
        ForecastData forecastData = getDataManager().getForecastReport();

        if (forecastData != null){
            getMvpView().populateDataInUI(forecastData);
        }else {
            Log.d(TAG,"no forecast report found");
        }
    }

    @Override
    public void makeServerCallForecast(int days, final String latLng) {
        Log.d(TAG,"Making Server call for forevast");
        RestClient.getApiServicePojo()
                .getWeatherForecastWithDays(BuildConfig.API_KEY,
                        latLng,
                        String.valueOf(days), new Callback<ForecastData>() {
                            @Override
                            public void success(ForecastData forecastData, Response response) {
                                Log.d(TAG,"Success : "+new Gson().toJson(forecastData));
                                getMvpView().hideLoading();
                                getMvpView().populateDataInUI(forecastData);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d(TAG,"failure : "+error.toString());
                                getMvpView().hideLoading();
                                getMvpView().showErrorDialog("Something went wrong at our end!", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getMvpView().dismissErrDialog();
                                        getMvpView().showLoading();
                                        getMvpView().makeServerCall(latLng);
                                    }
                                });
                            }
                        });
    }
}
