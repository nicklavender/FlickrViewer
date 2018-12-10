package com.lavender.nick.flickrviewer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lavender.nick.flickrviewer.FlickrViewerConstants;
import com.lavender.nick.flickrviewer.R;
import com.lavender.nick.flickrviewer.model.AppFlickrPhoto;
import com.squareup.picasso.Picasso;


public class FlickrPhotoPageFragment extends Fragment {

    private String TAG = FlickrPhotoPageFragment.class.getSimpleName();
    private AppFlickrPhoto mFlickrPhoto;
    private TextView mPhotoTitle;
    private ImageView mPhoto;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mFlickrPhoto = args.getParcelable(FlickrViewerConstants.FLICKR_PHOTO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_flickr_photo_page, container, false);
        mPhotoTitle = rootView.findViewById(R.id.photo_title);
        mPhoto = rootView.findViewById(R.id.photo);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mFlickrPhoto != null) {
            mPhotoTitle.setText(mFlickrPhoto.getPhotoTitle());
            Picasso
                    .with(getContext())
                    .load(mFlickrPhoto.getPhotoLink())
                    .fit()
                    .centerInside()
                    .into(mPhoto);
        }
    }
}
