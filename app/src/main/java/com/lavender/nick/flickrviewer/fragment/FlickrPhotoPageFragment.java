package com.lavender.nick.flickrviewer.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.lavender.nick.flickrviewer.FlickrViewerConstants;
import com.lavender.nick.flickrviewer.R;
import com.lavender.nick.flickrviewer.model.AppFlickrPhoto;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FlickrPhotoPageFragment extends Fragment {

    private String TAG = FlickrPhotoPageFragment.class.getSimpleName();
    private AppFlickrPhoto mFlickrPhoto;
    private TextView mPhotoTitle;
    private TextView mMLText;
    private TextView mMLLabels;
    private ImageView mPhoto;
    private Bitmap mBitmap;
    private Target mTarget;


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
        mMLText = rootView.findViewById(R.id.ml_detected_text);
        mMLLabels = rootView.findViewById(R.id.ml_detected_labels);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mFlickrPhoto != null) {
            String title = mFlickrPhoto.getPhotoTitle();
            mPhotoTitle.setText(title == null || title.trim().length() == 0 ? "untitled" : title);
            mTarget = new Target() {
                @Override
                public void onBitmapLoaded (final Bitmap bitmap, final Picasso.LoadedFrom from) {
                    mBitmap = bitmap;
                    mPhoto.setImageBitmap(mBitmap);
                    getMLText();
                }



                @Override
                public void onBitmapFailed (final Exception e, final Drawable errorDrawable) {

                }



                @Override
                public void onPrepareLoad (final Drawable placeHolderDrawable) {

                }
            };
            Picasso
                    .get()
                    .load(mFlickrPhoto.getPhotoLink())
                    .resize(300, 300)
                    .centerInside()
                    .into(mTarget);
        }

    }



    private void getMLText () {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(mBitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        detector.processImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess (FirebaseVisionText firebaseVisionText) {
                        mMLText.setText(firebaseVisionText.getText());
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure (@NonNull Exception e) {
                                Log.d(TAG, e.getLocalizedMessage());
                            }
                        });
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getOnDeviceImageLabeler();
        labeler.processImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess (List<FirebaseVisionImageLabel> labels) {
                        StringBuilder labelsText = new StringBuilder();
                        int count = 0;
                        for (FirebaseVisionImageLabel label : labels) {
                            String text = label.getText();
                            float confidence = label.getConfidence();
                            if (count < 5) {
                                labelsText.append(text).append(" : ").append(confidence).append(System.getProperty("line.separator"));
                                count++;
                            }
                        }
                        mMLLabels.setText(labelsText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure (@NonNull Exception e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });

    }
}
