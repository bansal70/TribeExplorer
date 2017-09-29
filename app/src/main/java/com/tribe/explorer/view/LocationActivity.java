package com.tribe.explorer.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.tribe.explorer.R;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = LocationActivity.class.getSimpleName();
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 10001;
    GoogleApiClient mGoogleApiClient;
    LocationManager manager;
    Dialog dialog;
    private GoogleMap mGoogleMap;
    CardView cardSearch;
    TextView tvSearch;
    Button btnLocation;
    String address = "";
    double latitude = 0.0, longitude = 0.0;
    Location location;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
        }

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();

        initViews();
    }

    private void initViews() {
        dialog = Utils.showDialog(this);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        cardSearch = (CardView) findViewById(R.id.cardSearch);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        btnLocation = (Button) findViewById(R.id.btnLocation);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        cardSearch.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        enableLoc();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardSearch:
                findPlace();
                break;

            case R.id.btnLocation:
                if (address.isEmpty()) {
                    Toast.makeText(this, R.string.select_location, Toast.LENGTH_SHORT).show();
                }
                else {
                    Utils.location = address;
                    finish();
                }
                break;

            case R.id.imgBack:
                finish();
                break;
        }
    }

    public void findPlace() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    Log.i(TAG, "Place: " + place.getName());
                    LatLng latLng = place.getLatLng();
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    Log.i(TAG, status.getStatusMessage());
                }
                break;

            case com.tribe.explorer.controller.LocationManager.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getCurrentLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.LOCATION_SUCCESS:
                double lat = TEPreferences.readDouble(this, "latitude");
                double lng = TEPreferences.readDouble(this, "longitude");
                LatLng latLng = new LatLng(lat, lng);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                break;

            case Constants.LOCATION_EMPTY:
                break;
        }
    }

    @Override
    public void onCameraIdle() {
        LatLng latLng = mGoogleMap.getCameraPosition().target;
        Log.e("Location", "LAT: "+latLng.latitude);
        address = Utils.getCompleteAddressString(this, latLng.latitude, latLng.longitude);
        tvSearch.setText(address);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        try {
            boolean success = mGoogleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            if (!success) {
                Log.e("sorry try again", "Style parsing failed.");
            }
        }catch (Resources.NotFoundException e){
            e.printStackTrace();
        }

        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(latitude, longitude))
                .zoom(15)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        mGoogleMap.setOnCameraIdleListener(this);

        if (mGoogleMap != null)
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));

    }

    private void enableLoc() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            } else {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getCurrentLocation();
                    return;
                }
                ModelManager.getInstance().getLocationManager()
                        .requestLocation(this, mGoogleApiClient);
            }
        } else {
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getCurrentLocation();
                return;
            }
            ModelManager.getInstance().getLocationManager()
                    .requestLocation(this, mGoogleApiClient);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ModelManager.getInstance().getLocationManager()
                        .requestLocation(this, mGoogleApiClient);
            } else {
                Toast.makeText(this, "Please grant all the permissions.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please grant all the permissions.", Toast.LENGTH_SHORT).show();
        }
    }

    public void getCurrentLocation() {
        dialog.show();
        ModelManager.getInstance().getLocationManager()
                .startLocationUpdates(this, mGoogleApiClient);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();

                    if (ContextCompat.checkSelfPermission(LocationActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        location = manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        if (location == null)
                            Toast.makeText(LocationActivity.this, R.string.location_empty,
                                    Toast.LENGTH_SHORT).show();
                        else {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        }
                    }
                }
            }
        }, 5000);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
