package com.lavender.nick.flickrviewer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.lavender.nick.flickrviewer.adapter.FlickrPhotoPagerAdapter;
import com.lavender.nick.flickrviewer.api.FlickrApi;
import com.lavender.nick.flickrviewer.api.response.FlickrFeed;
import com.lavender.nick.flickrviewer.api.response.Item;
import com.lavender.nick.flickrviewer.model.AppFlickrPhoto;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends FragmentActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager mPager;
    private FlickrPhotoPagerAdapter mPagerAdapter;
    private SwipeRefreshLayout mSwipeToFreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeToFreshLayout = findViewById(R.id.swipe_to_refresh);
        mSwipeToFreshLayout.setOnRefreshListener(new FlickrSwipeToRefreshListener());
        mPager = findViewById(R.id.view_pager);
        getFlickrFeed();
    }

    private void getFlickrFeed() {
        Log.d(TAG, "getFlickrFeed");
        OkHttpClient client = new OkHttpClient.Builder().build();
        FlickrApi.flickrApiService(client).getFlickrPhotos().enqueue(new retrofit2.Callback<FlickrFeed>() {
            @Override
            public void onResponse(@NonNull Call<FlickrFeed> call, @NonNull Response<FlickrFeed> response) {
                FlickrFeed flickrFeed = response.body();
                if(flickrFeed != null) {
                    String feedTitle = flickrFeed.getTitle();
                    String feedLink = flickrFeed.getLink();
                    String feedDescription = flickrFeed.getDescription();
                    List<AppFlickrPhoto> flickrPhotos = new ArrayList<>(FlickrViewerConstants.PAGE_SIZE);
                    for (Item item : flickrFeed.getItems()) {
                        AppFlickrPhoto appFlickrPhoto = new AppFlickrPhoto(feedTitle, feedLink, feedDescription, item);
                        flickrPhotos.add(appFlickrPhoto);
                    }
                    mPagerAdapter = new FlickrPhotoPagerAdapter(getSupportFragmentManager());
                    mPager.setAdapter(mPagerAdapter);
                    mPagerAdapter.setPhotos(flickrPhotos);
                    Log.d(TAG, "Flickr feed retrieved, count: " + flickrPhotos.size());
                } else {
                    Log.d(TAG, "Null response");
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlickrFeed> call, @NonNull Throwable t) {
                Log.d(TAG, "Flickr feed retrieval onFailure: " + t.getMessage());
            }
        });
    }

    class FlickrSwipeToRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            getFlickrFeed();
            mSwipeToFreshLayout.setRefreshing(false);
        }
    }
}
