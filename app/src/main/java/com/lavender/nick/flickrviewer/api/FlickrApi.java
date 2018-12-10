package com.lavender.nick.flickrviewer.api;

import com.lavender.nick.flickrviewer.FlickrViewerConstants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlickrApi {

    private static FlickrApiService builder(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(FlickrViewerConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(FlickrApiService.class);
    }

    public static FlickrApiService flickrApiService(OkHttpClient client) {
        return builder(client);
    }
}
