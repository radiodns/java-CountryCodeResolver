/*
 * Copyright (c) 2013 Global Radio UK Limited
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.radiodns.android.gcc;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.radiodns.gcc.ResolutionException;
import org.radiodns.gcc.Resolver;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This simple Activity will attempt to determine the device location and the
 * corresponding ISO Country Code. The GCC can then be resolved with the ISO
 * code and given RDS PI Code.
 * 
 * The ISO Country Code is obtained using the Geocoder class. Alternatively it
 * can be typed into the UI.
 * 
 * @author Byrion Smith <byrion.smith@thisisglobal.com>
 * @version 0.1
 */
public class MainActivity extends Activity {

	static final String TAG = "MainActivity";

	static final int UPDATE_COUNTRY_CODE = 0x201;

	Context mCtx;
	LocationManager mLocationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mCtx = getApplicationContext();

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationManager.removeUpdates(mListener);
		
	};

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_COUNTRY_CODE:
				String countryCode = (String) msg.obj;
				Log.d(TAG, "Country Code: " + countryCode);

				// update UI
				EditText edtCountryCode = (EditText) findViewById(R.id.edtCountryCode);
				edtCountryCode.setText(countryCode);

				break;
			}
			super.handleMessage(msg);
		}
	};

	public void onUIClick(View v) {
		switch (v.getId()) {
		case R.id.btnDetermineLocation:

			// try and get the last known location
			Location location = mLocationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			// Reverse-geocoding is only possible if the Geocoder service is
			// available on the device
			if (location != null && Geocoder.isPresent())
				(new ReverseGeocodingTask(mCtx))
						.execute(new Location[] { location });

			// listen for location updates
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1, 1, mListener);

			break;
		case R.id.btnResolveGCC:

			// grab the country code and PI code from the text fields and supply
			// them to the GCC Resolver library
			
			TextView txtGCC = (TextView) findViewById(R.id.txtGCC);
			try {

				EditText edtRdsPi = (EditText) findViewById(R.id.edtPICode);
				String rdsPi = edtRdsPi.getText().toString();

				EditText edtCountryCode = (EditText) findViewById(R.id.edtCountryCode);
				String countryCode = edtCountryCode.getText().toString();

				Resolver resolver = new Resolver();				
				String gcc = resolver.getGCC(countryCode, rdsPi);

				Log.d(TAG, String.format("GCC: %s (%s, %s)", gcc, countryCode,
						rdsPi));

				txtGCC.setText(String.format("%s (%s, %s)", gcc, countryCode,
						rdsPi));

			} catch (ResolutionException e) {
				e.printStackTrace();
				txtGCC.setText("Resolution Exception");
			}

			break;
		}

	}

	private final LocationListener mListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			Log.d(TAG, "onLocationChanged");
			// Reverse-geocoding is only possible if the Geocoder service is
			// available on the device
			if (Geocoder.isPresent()) {
				// Since the geocoding API is synchronous and may take a while.
				// You don't want to lock
				// up the UI thread. Invoking reverse geocoding in an AsyncTask.
				(new ReverseGeocodingTask(mCtx))
						.execute(new Location[] { location });
			}
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	};

	private class ReverseGeocodingTask extends AsyncTask<Location, Void, Void> {
		Context mContext;

		public ReverseGeocodingTask(Context context) {
			super();
			mContext = context;
		}

		@Override
		protected Void doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

			Location loc = params[0];
			List<Address> addresses = null;
			try {
				// Call the synchronous getFromLocation() method by passing in
				// the lat/long values.
				addresses = geocoder.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				Message.obtain(mHandler, UPDATE_COUNTRY_CODE,
						address.getCountryCode()).sendToTarget();
			}
			return null;
		}
	}
}