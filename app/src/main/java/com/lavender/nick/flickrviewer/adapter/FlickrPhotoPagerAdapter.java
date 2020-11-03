package com.lavender.nick.flickrviewer.adapter;

import android.os.Bundle;
import android.util.Log;

import com.lavender.nick.flickrviewer.FlickrViewerConstants;
import com.lavender.nick.flickrviewer.fragment.FlickrPhotoPageFragment;
import com.lavender.nick.flickrviewer.model.AppFlickrPhoto;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class FlickrPhotoPagerAdapter extends FragmentStatePagerAdapter {

    private final String TAG = FlickrPhotoPagerAdapter.class.getSimpleName();

    @Nullable
    private List<AppFlickrPhoto> mPhotos;

    @Inject
    public FlickrPhotoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
            FlickrPhotoPageFragment flickrPhotoPageFragment = new FlickrPhotoPageFragment();
            if(mPhotos != null) {
                Log.d(TAG, "getItem making bundle mPhotos not null: name is: " + mPhotos.get(i).getPhotoTitle());
                Bundle bundle = new Bundle();
                bundle.putParcelable(FlickrViewerConstants.FLICKR_PHOTO, mPhotos.get(i));
                flickrPhotoPageFragment.setArguments(bundle);
            }
            return flickrPhotoPageFragment;
    }

    @Override
    public int getCount() {
        return FlickrViewerConstants.PAGE_SIZE;
    }

    // san >



    /**
     * test
     * @param mPhotos asdad
     */
    public void setPhotos(List<AppFlickrPhoto> mPhotos) {
        this.mPhotos = mPhotos;
    }
}
