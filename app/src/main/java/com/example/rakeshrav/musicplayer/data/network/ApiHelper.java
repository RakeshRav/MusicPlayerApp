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

package com.example.rakeshrav.musicplayer.data.network;

import com.example.rakeshrav.musicplayer.data.network.model.itunesData.ItunesData;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by rao .
 */

public interface ApiHelper {

    @GET("/search")
    void getSongsList(@Query("term") String searchTerm,
                                    @Query("limit") String limit,
                                    @Query("media") String music,
                                    Callback<ItunesData> callback);
}
