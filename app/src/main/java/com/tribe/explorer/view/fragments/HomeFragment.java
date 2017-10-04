package com.tribe.explorer.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.tribe.explorer.R;
import com.tribe.explorer.controller.HomeManager;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.CategoriesData;
import com.tribe.explorer.view.adapters.HomeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class HomeFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    RecyclerView recyclerCategories;
    HomeAdapter homeAdapter;
    private List<CategoriesData.Data> categoriesList;
    Dialog dialog;
    ImageView imgLocation, imgSearch;
    LinearLayout searchLayout;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 10001;
    GoogleApiClient mGoogleApiClient;
    LocationManager manager;
    Location location;
    TextView tvLocation, tvSearch;
    EditText editQuery;
    String lat = "", lng = "";
    String lang, user_id;
    RecyclerView.OnScrollListener onScrollListener;
    int page = 1;
    boolean isSearch = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user_id = TEPreferences.readString(getActivity(), "user_id");
        lang = TEPreferences.readString(getActivity(), "lang");

        manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
        }

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View v) {
        dialog = Utils.showDialog(getActivity());
        categoriesList = new ArrayList<>();
        dialog.show();
        ModelManager.getInstance().getHomeManager().categoriesTask(Operations.getCategoriesParams(lang, 1));

        searchLayout = v.findViewById(R.id.searchLayout);
        imgLocation = v.findViewById(R.id.imgLocation);
        imgSearch = v.findViewById(R.id.imgSearch);
        tvLocation = v.findViewById(R.id.tvLocation);
        tvSearch = v.findViewById(R.id.tvSearch);
        editQuery = v.findViewById(R.id.editQuery);
        recyclerCategories = v.findViewById(R.id.recyclerCategories);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getActivity()));

        //categoriesList.addAll(HomeManager.categoriesList);
        homeAdapter = new HomeAdapter(getActivity(), categoriesList);
        recyclerCategories.setAdapter(homeAdapter);

        imgLocation.setOnClickListener(this);
        tvLocation.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        loadMore();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgLocation:
                enableLoc();
                break;

            case R.id.tvLocation:
                findPlace();
                break;

            case R.id.tvSearch:
                selectFragment(new HomeListingFragment());
                break;

            case R.id.imgSearch:
                if (isSearch) {
                    imgSearch.setImageResource(R.mipmap.search_blue);
                    isSearch = false;
                    searchLayout.setVisibility(View.GONE);
                } else {
                    imgSearch.setImageResource(R.drawable.ic_cancel);
                    isSearch = true;
                    searchLayout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void findPlace() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
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
                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                    Log.i(TAG, "Place: " + place.getName());
                    tvLocation.setText(place.getName());
                    LatLng latLng = place.getLatLng();
                    lat = String.valueOf(latLng.latitude);
                    lng = String.valueOf(latLng.longitude);
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                    Log.i(TAG, status.getStatusMessage());
                }
                break;

            case com.tribe.explorer.controller.LocationManager.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        getCurrentLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    private void enableLoc() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            } else {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getCurrentLocation();
                    return;
                }
                ModelManager.getInstance().getLocationManager()
                        .requestLocation(getActivity(), mGoogleApiClient);
            }
        } else {
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getCurrentLocation();
                return;
            }
            ModelManager.getInstance().getLocationManager()
                    .requestLocation(getActivity(), mGoogleApiClient);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getCurrentLocation();
                    return;
                }
                ModelManager.getInstance().getLocationManager()
                        .requestLocation(getActivity(), mGoogleApiClient);
            } else {
                Toast.makeText(getActivity(), R.string.please_grant_permissions, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.please_grant_permissions, Toast.LENGTH_SHORT).show();
        }
    }

    public void getCurrentLocation() {
        dialog.show();
        ModelManager.getInstance().getLocationManager()
                .startLocationUpdates(getActivity(), mGoogleApiClient);
        location = manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();

                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (location == null)
                            Toast.makeText(getActivity(), R.string.location_empty,
                                    Toast.LENGTH_SHORT).show();
                        else {
                            String loc = Utils.getCompleteAddressString(getActivity(),
                                    location.getLatitude(), location.getLongitude());
                            lat = String.valueOf(location.getLatitude());
                            lng = String.valueOf(location.getLongitude());
                            tvLocation.setText(loc);
                        }
                    }
                }
            }
        }, 5000);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onEvent(Event event) {
        dialog.dismiss();
        EventBus.getDefault().removeAllStickyEvents();
        switch (event.getKey()) {
            case Constants.CATEGORIES_SUCCESS:
                categoriesList.addAll(HomeManager.categoriesList);
                homeAdapter.notifyDataSetChanged();
                break;

            case Constants.CATEGORIES_ERROR:
                recyclerCategories.removeOnScrollListener(onScrollListener);
                break;

            case Constants.LOCATION_SUCCESS:
                lat = String.valueOf(TEPreferences.readDouble(getActivity(), "latitude"));
                lng = String.valueOf(TEPreferences.readDouble(getActivity(), "longitude"));
                tvLocation.setText(TEPreferences.readString(getActivity(), "location"));
                break;

            case Constants.LOCATION_EMPTY:
                break;

            case Constants.NO_RESPONSE:
                Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void loadMore() {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerCategories
                .getLayoutManager();

        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(linearLayoutManager.findLastCompletelyVisibleItemPosition()== categoriesList.size()-1)
                if (isAtBottom(recyclerCategories)) {
                    if (HomeManager.categoriesList.size() > 1) {
                        dialog.show();
                        HomeManager.categoriesList.clear();
                        ModelManager.getInstance().getHomeManager().categoriesTask(Operations
                                .getCategoriesParams(lang, ++page));
                    }
                }
            }
        };

        recyclerCategories.addOnScrollListener(onScrollListener);
    }

    public static boolean isAtBottom(RecyclerView recyclerView) {
        return !ViewCompat.canScrollVertically(recyclerView, 1);

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

    public void selectFragment(Fragment fragment) {
        String query = editQuery.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        bundle.putString("lat", lat);
        bundle.putString("lng", lng);
        bundle.putString("lang", lang);
        bundle.putString("user_id", user_id);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
