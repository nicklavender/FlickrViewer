package com.lavender.nick.flickrviewer;

import android.os.Bundle;

import com.lavender.nick.flickrviewer.fragment.PreferenceFragment;

import androidx.appcompat.app.AppCompatActivity;

public class PreferencesActivity extends AppCompatActivity {

	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences_activity_layout);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.preferences_activity_container, new PreferenceFragment())
				.commit();
	}

}
