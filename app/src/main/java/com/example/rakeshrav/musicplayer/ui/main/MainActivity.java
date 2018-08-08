package com.example.rakeshrav.musicplayer.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.rakeshrav.musicplayer.R;
import com.example.rakeshrav.musicplayer.data.network.model.forecastData.ForecastData;
import com.example.rakeshrav.musicplayer.ui.base.BaseActivity;
import com.example.rakeshrav.musicplayer.utility.CommonUtils;
import com.example.rakeshrav.musicplayer.utility.ScreenUtils;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainMvpView, PermissionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;
    @BindView(R.id.flTop)
    FrameLayout flTop;
    @BindView(R.id.rvWeather)
    RecyclerView rvWeather;
    @BindView(R.id.tvCurrentTemp)
    TextView tvCurrentTemp;
    @BindView(R.id.tvCurrentLocation)
    TextView tvCurrentLocation;
    @BindView(R.id.cvWeatherContent)
    CardView cvWeatherContent;
    @BindView(R.id.tvDegree)
    TextView tvDegree;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScreenUtils.setHideWindow(this);

        setContentView(R.layout.activity_main_weather);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        setUp();

        checkForLocationPermission();
    }

    private void checkForLocationPermission() {
        TedPermission.with(this)
                .setPermissionListener(this)
                .setDeniedMessage("If you reject permission,you can not use this app\n\nPlease turn on permissions")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }


    @Override
    protected void setUp() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvWeather.setLayoutManager(manager);

        //animate weather view
        cvWeatherContent.animate().translationYBy(1000).setDuration(0).start();
    }

    @Override
    public void populateDataInUI(ForecastData forecastData) {
        Log.d(TAG, "populate Data");

        if (!CommonUtils.isNullOrEmpty(forecastData.getLocation().getName())) {
            tvCurrentLocation.setText(forecastData.getLocation().getName());
        }

        tvCurrentTemp.setText(String.valueOf(forecastData.getCurrent().getTempC().intValue()));
        tvDegree.setVisibility(View.VISIBLE);
        rvWeather.setAdapter(new ForecastAdapter(this, forecastData));

        cvWeatherContent.animate().translationYBy(-1000).setDuration(1200).start();
    }

    @Override
    public void makeServerCall(final String latLng) {
        if (isNetworkConnected()) {
            mPresenter.makeServerCallForecast(4, latLng);
        } else {
            showErrorDialog("No Internet Connection Available", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissErrDialog();
                    makeServerCall(latLng);
                }
            });
        }
    }

    @Override
    public void onPermissionGranted() {
        Log.d(TAG,"onPermissionGranted");

        final LocationCallback locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    hideLoading();
                    showErrorDialog("Last Location Not Found!", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismissErrDialog();
                            checkForLocationPermission();
                        }
                    });
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    String latLng = location.getLatitude()+","+location.getLongitude();
                    Log.d(TAG,"latLng : "+latLng);
                    removeLocationCallBack();
                    makeServerCall(latLng);
                }
            };
        };

        listenToLastLocation(locationCallback);
    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        Log.d(TAG,"onPermissionDenied");
        showErrorDialog("Please grant location permission.", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissErrDialog();
                checkForLocationPermission();
            }
        });
    }
}
