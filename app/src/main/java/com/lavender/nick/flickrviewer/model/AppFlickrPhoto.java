package com.lavender.nick.flickrviewer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lavender.nick.flickrviewer.api.response.Item;

public class AppFlickrPhoto implements Parcelable {

    //General fields from Flickr feed
    private final String mFeedTitle;
    private final String mFeedLink;
    private final String mFeedDescription;
    //Fields for this photo
    private final String mPhotoTitle;
    private final String mPhotoLink;
    private final String mPhotoDescription;

    public AppFlickrPhoto(String feedTitle, String feedLink, String feedDescription, Item item) {
        mFeedTitle = feedTitle;
        mFeedLink = feedLink;
        mFeedDescription = feedDescription;
        mPhotoTitle = item.getTitle();
        mPhotoLink = item.getMedia().getM();
        mPhotoDescription = item.getDescription();
    }


    //Unused methods, likely used if app was expanded
    public String getFeedTitle() {
        return mFeedTitle;
    }

    public String getFeedLink() {
        return mFeedLink;
    }

    public String getFeedDescription() {
        return mFeedDescription;
    }

    public String getPhotoTitle() {
        return mPhotoTitle;
    }

    public String getPhotoLink() {
        return mPhotoLink;
    }

    public String getPhotoDescription() {
        return mPhotoDescription;
    }


    protected AppFlickrPhoto(Parcel in) {
        mFeedTitle = in.readString();
        mFeedLink = in.readString();
        mFeedDescription = in.readString();
        mPhotoTitle = in.readString();
        mPhotoLink = in.readString();
        mPhotoDescription = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFeedTitle);
        dest.writeString(mFeedLink);
        dest.writeString(mFeedDescription);
        dest.writeString(mPhotoTitle);
        dest.writeString(mPhotoLink);
        dest.writeString(mPhotoDescription);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AppFlickrPhoto> CREATOR = new Parcelable.Creator<AppFlickrPhoto>() {
        @Override
        public AppFlickrPhoto createFromParcel(Parcel in) {
            return new AppFlickrPhoto(in);
        }

        @Override
        public AppFlickrPhoto[] newArray(int size) {
            return new AppFlickrPhoto[size];
        }
    };
}