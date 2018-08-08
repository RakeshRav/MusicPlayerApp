/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.example.rakeshrav.musicplayer.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.rakeshrav.musicplayer.data.network.ApiHelper;
import com.example.rakeshrav.musicplayer.data.network.AppApiHelper;
import com.example.rakeshrav.musicplayer.di.ActivityContext;
import com.example.rakeshrav.musicplayer.di.PerActivity;
import com.example.rakeshrav.musicplayer.ui.main.MainMvpPresenter;
import com.example.rakeshrav.musicplayer.ui.main.MainMvpView;
import com.example.rakeshrav.musicplayer.ui.main.MainPresenter;
import com.example.rakeshrav.musicplayer.ui.splash.SplashMvpPresenter;
import com.example.rakeshrav.musicplayer.ui.splash.SplashPresenter;
import com.example.rakeshrav.musicplayer.ui.splash.SplashView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by rao .
 */

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> provideMainPresenter(MainPresenter<MainMvpView> mainPresenter){
        return mainPresenter;
    }

    @Provides
    @PerActivity
    SplashMvpPresenter<SplashView> providesSplashPresenter(SplashPresenter<SplashView> splashPresenter){
        return splashPresenter;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }
}
