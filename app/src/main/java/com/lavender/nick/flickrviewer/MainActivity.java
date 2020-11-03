package com.lavender.nick.flickrviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.firebase.FirebaseApp;
import com.lavender.nick.flickrviewer.adapter.FlickrPhotoPagerAdapter;
import com.lavender.nick.flickrviewer.animation.DepthPageTransformer;
import com.lavender.nick.flickrviewer.animation.ZoomOutPageTransformer;
import com.lavender.nick.flickrviewer.api.FlickrApi;
import com.lavender.nick.flickrviewer.api.response.FlickrFeed;
import com.lavender.nick.flickrviewer.api.response.Item;
import com.lavender.nick.flickrviewer.model.AppFlickrPhoto;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private ViewPager mPager;
	@Inject
	private FlickrPhotoPagerAdapter mPagerAdapter;
	private SwipeRefreshLayout mSwipeToFreshLayout;
	private SharedPreferences sharedPreferences;



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FirebaseApp.initializeApp(this);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		setTitle("");
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		mSwipeToFreshLayout = findViewById(R.id.swipe_to_refresh);
		mSwipeToFreshLayout.setOnRefreshListener(new FlickrSwipeToRefreshListener());
		mPager = findViewById(R.id.view_pager);

		getFlickrFeed();
	}



	@Override
	protected void onResume () {
		super.onResume();
		boolean rotation = sharedPreferences.getBoolean(Constants.ROTATION_PREF, false);
		if (sharedPreferences.getInt(Constants.ANIMATION_PREF, 0) == (Constants.ANIMATION_ZOOM_OUT)) {
			mPager.setPageTransformer(true, new ZoomOutPageTransformer(rotation));
		} else {
			mPager.setPageTransformer(true, new DepthPageTransformer(rotation));
		}
	}



	@Override
	public boolean onCreateOptionsMenu (final Menu menu) {
		getMenuInflater().inflate(R.menu.toolbar_menu, menu);
		return true;
	}



	@Override
	public boolean onOptionsItemSelected (final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.preferences_activity:
				Intent intent = new Intent(this, PreferencesActivity.class);
				startActivity(intent);
				break;
			case R.id.license_activity:
				startActivity(new Intent(getApplicationContext(), OssLicensesMenuActivity.class));
				break;
		}
		return true;
	}



	private void getFlickrFeed () {
		Log.d(TAG, "getFlickrFeed");
		OkHttpClient client = new OkHttpClient.Builder().build();
		FlickrApi.flickrApiService(client).getFlickrPhotos().enqueue(new retrofit2.Callback<FlickrFeed>() {
			@Override
			public void onResponse (@NonNull Call<FlickrFeed> call, @NonNull Response<FlickrFeed> response) {
				FlickrFeed flickrFeed = response.body();
				if (flickrFeed != null) {
					String feedTitle = flickrFeed.getTitle();
					String feedLink = flickrFeed.getLink();
					String feedDescription = flickrFeed.getDescription();
					List<AppFlickrPhoto> flickrPhotos = new ArrayList<>(FlickrViewerConstants.PAGE_SIZE);
					for (Item item : flickrFeed.getItems()) {
						AppFlickrPhoto appFlickrPhoto = new AppFlickrPhoto(feedTitle, feedLink, feedDescription, item);
						flickrPhotos.add(appFlickrPhoto);
					}
					//mPagerAdapter = new FlickrPhotoPagerAdapter(getSupportFragmentManager());
					mPagerAdapter.setPhotos(flickrPhotos);
					mPager.setAdapter(mPagerAdapter);
					Log.d(TAG, "Flickr feed retrieved, count: " + flickrPhotos.size());
				} else {
					Log.d(TAG, "Null response");
				}
				mSwipeToFreshLayout.setRefreshing(false);
			}



			@Override
			public void onFailure (@NonNull Call<FlickrFeed> call, @NonNull Throwable t) {
				Log.d(TAG, "Flickr feed retrieval onFailure: " + t.getMessage());
			}
		});
	}



	class FlickrSwipeToRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

		@Override
		public void onRefresh () {
			getFlickrFeed();
		}
	}

}
