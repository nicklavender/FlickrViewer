package com.lavender.nick.flickrviewer.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lavender.nick.flickrviewer.Constants;
import com.lavender.nick.flickrviewer.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class PreferenceFragment extends PreferenceFragmentCompat {


	private SharedPreferences sharedPreferences;



	@Override
	public void onCreatePreferences (final Bundle savedInstanceState, final String rootKey) {
		setPreferencesFromResource(R.xml.preferences_main_layout, rootKey);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
	}



	@Override
	public View onCreateView (
			@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState)
	{

		ListPreference animationStyle = getPreferenceManager().findPreference("animation_styles");
		animationStyle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange (final Preference preference, final Object newValue) {
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putInt(Constants.ANIMATION_PREF, (int) newValue);
				editor.apply();
				return false;
			}
		});

		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
