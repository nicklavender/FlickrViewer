package com.lavender.nick.flickrviewer;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;

public class SampleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_demo);

		SupportMapFragment mapFragment =
				(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we
	 * just add a marker near Africa.
	 */
	@Override
	public void onMapReady(GoogleMap map) {
		map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
	}
}
