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

package com.example.rakeshrav.musicplayer.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.rakeshrav.musicplayer.data.network.model.itunesData.ItunesData;
import com.example.rakeshrav.musicplayer.data.network.model.itunesData.Result;
import com.example.rakeshrav.musicplayer.di.ApplicationContext;
import com.example.rakeshrav.musicplayer.di.PreferenceInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by rao .
 * define and access all the data related to preference manager
 */

@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";
    private static final String PREF_KEY_FAVS = "PREF_KEY_FAVS";

    private static Gson GSON = new Gson();

    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context,
                                @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public String getCurrentUserName() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_NAME, null);
    }

    @Override
    public void setCurrentUserName(String userName) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply();
    }

    @Override
    public ItunesData getFavsResult() {
        String gson = mPrefs.getString(PREF_KEY_FAVS, null);
        if (gson == null) {
            return null;
        } else {
            try {
                return GSON.fromJson(gson, ItunesData.class);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object storaged with key "
                        + PREF_KEY_FAVS + " is instanceof other class");
            }
        }
    }

    @Override
    public void setFavsResult(ItunesData object) {
        if (object == null) {
            throw new IllegalArgumentException("object is null");
        }

        mPrefs.edit().putString(PREF_KEY_FAVS, GSON.toJson(object)).apply();
    }

}
