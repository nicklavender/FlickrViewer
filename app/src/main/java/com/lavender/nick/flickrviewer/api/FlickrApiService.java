package com.lavender.nick.flickrviewer.api;

import com.lavender.nick.flickrviewer.api.response.FlickrFeed;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FlickrApiService {

    @GET("photos_public.gne?format=json&nojsoncallback=1")
    Call<FlickrFeed> getFlickrPhotos();

}
