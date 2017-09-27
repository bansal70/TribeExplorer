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
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tribe.explorer.R;
import com.tribe.explorer.controller.HomeManager;
import com.tribe.explorer.controller.LabelsManager;
import com.tribe.explorer.controller.ListingManager;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Config;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.CategoriesData;
import com.tribe.explorer.model.beans.LabelsData;
import com.tribe.explorer.model.beans.ListingData;
import com.tribe.explorer.model.custom.MySupportMapFragment;
import com.tribe.explorer.view.adapters.CategoriesAdapter;
import com.tribe.explorer.view.adapters.ListingAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.tribe.explorer.controller.HomeManager.categoriesList;

public class SearchFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, OnSeekbarChangeListener,
        OnMapReadyCallback, MySupportMapFragment.OnTouchListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private final String TAG = SearchFragment.class.getSimpleName();

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 10001;
    GoogleApiClient mGoogleApiClient;
    LocationManager manager;
    Location location;

    EditText editQuery;
    TextView tvLocation, tvCategory;
    TextView tvRadius, tvSearch, tvListings, tvMap, tvSortBy;
    Spinner spinnerFilters;
    CrystalSeekbar seekRadius;
    String lat = "", lng = "", radius = "";
    ImageView imgLocation;

    RecyclerView recyclerListing;
    private ListingAdapter listingAdapter;
    private Dialog dialog, dialogCategories;
    TextView tvCancel, tvConfirm;
    private List<ListingData.Data> listData;
    String user_id, lang;
    MySupportMapFragment mapFragment;
    CardView cardMap;
    NestedScrollView nestedScrollView;
    private ArrayList<Double> latList, lngList;
    GoogleMap mGoogleMap;
    private List<String> labelsDataList;
    private ArrayAdapter labelsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
        }

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();


        dialog = Utils.showDialog(getActivity());
        listData = new ArrayList<>();
        user_id = TEPreferences.readString(getActivity(), "user_id");
        lang = TEPreferences.readString(getActivity(), "lang");
        ModelManager.getInstance().getLabelsManager()
                .labelsTask(getActivity(), Config.LABEL_LIST_URL+lang);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);

        return view;
    }

    public void initViews(View view) {
        latList = new ArrayList<>();
        lngList = new ArrayList<>();
        labelsDataList = new ArrayList<>();
        labelsDataList.add(getString(R.string.filter_results));

        tvSearch = view.findViewById(R.id.tvSearch);
        editQuery = view.findViewById(R.id.editQuery);
        tvLocation = view.findViewById(R.id.tvLocation);
        imgLocation = view.findViewById(R.id.imgLocation);
        tvCategory = view.findViewById(R.id.tvCategory);
        spinnerFilters = view.findViewById(R.id.spinnerFilters);
        tvSortBy = view.findViewById(R.id.tvSortBy);
        seekRadius = view.findViewById(R.id.seekRadius);
        tvRadius = view.findViewById(R.id.tvRadius);
        tvListings = view.findViewById(R.id.tvListings);

        tvMap = view.findViewById(R.id.tvMap);
        cardMap = view.findViewById(R.id.cardMap);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);

        recyclerListing = view.findViewById(R.id.recyclerListings);
        recyclerListing.setHasFixedSize(true);
        recyclerListing.setNestedScrollingEnabled(false);
        recyclerListing.setLayoutManager(new LinearLayoutManager(getActivity()));

        listingAdapter = new ListingAdapter(getActivity(), listData);
        recyclerListing.setAdapter(listingAdapter);

        imgLocation.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        tvLocation.setOnClickListener(this);
        tvCategory.setOnClickListener(this);
        tvMap.setOnClickListener(this);
        tvSortBy.setOnClickListener(this);
        tvListings.setOnClickListener(this);
        seekRadius.setOnSeekbarChangeListener(this);
        spinnerFilters.setOnItemSelectedListener(this);

        mapFragment = (MySupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment != null)
            mapFragment.setListener(this);

        labelsAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, labelsDataList);
        labelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilters.setAdapter(labelsAdapter);

        initDialog();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void initDialog() {
        dialogCategories = Utils.createDialog(getActivity(), R.layout.dialog_spinner_items);
        tvCancel = dialogCategories.findViewById(R.id.tvCancel);
        tvConfirm = dialogCategories.findViewById(R.id.tvConfirm);
        RecyclerView recyclerCategories = dialogCategories.findViewById(R.id.recyclerCategories);
        recyclerCategories.setHasFixedSize(true);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getActivity()));

        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getActivity(),
                categoriesList);
        recyclerCategories.setAdapter(categoriesAdapter);

        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    public void setCategories() {
        dialogCategories.dismiss();
        ArrayList<String> categories = new ArrayList<>();
        for (CategoriesData.Data data : HomeManager.categoriesList) {
            if (data.isSelected()) {
                categories.add(data.name);
            }
        }
        tvCategory.setText(categories.toString().replace("[", "").replace("]", ""));
    }

    @Override
    public void valueChanged(Number minValue) {
        radius = String.valueOf(minValue)+"";
        tvRadius.setText(getString(R.string.radius) + " " + String.valueOf(minValue));
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

            case R.id.tvCategory:
                dialogCategories.show();
                break;

            case R.id.tvSortBy:
                break;

            case R.id.tvConfirm:
                setCategories();
                break;

            case R.id.tvCancel:
                dialogCategories.dismiss();
                break;

            case R.id.tvSearch:
                searchListings();
                break;

            case R.id.tvListings:
                recyclerListing.setVisibility(View.VISIBLE);
                cardMap.setVisibility(View.GONE);
                setBackground(R.color.colorPrimary, R.color.colorWhite);
                setTextColor(R.color.colorWhite, R.color.colorPrimary);
                break;

            case R.id.tvMap:
                recyclerListing.setVisibility(View.GONE);
                cardMap.setVisibility(View.VISIBLE);
                setBackground(R.color.colorWhite, R.color.colorPrimary);
                setTextColor(R.color.colorPrimary, R.color.colorWhite);
                break;
        }
    }

    public void setBackground(int a, int b) {
        tvListings.setBackgroundResource(a);
        tvMap.setBackgroundResource(b);
    }

    public void setTextColor(int a, int b) {
        tvListings.setTextColor(ContextCompat.getColor(getActivity(), a));
        tvMap.setTextColor(ContextCompat.getColor(getActivity(), b));
    }

    public void searchListings() {
        if (mGoogleMap != null)
            mGoogleMap.clear();
        latList.clear();
        lngList.clear();
        listData.clear();
        listingAdapter.notifyDataSetChanged();

        String query = editQuery.getText().toString().trim();

        dialog.show();
        ModelManager.getInstance().getListingManager().listingTask(Operations
                .getSearchParams(query, lat, lng, radius, lang, user_id));
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
            case Constants.LISTING_SUCCESS:
                listData.addAll(ListingManager.dataList);
                listingAdapter.notifyDataSetChanged();

                for (ListingData.Data data : listData) {
                    if (!data.latitude.isEmpty() || !data.longitude.isEmpty()) {
                        latList.add(Double.parseDouble(data.latitude));
                        lngList.add(Double.parseDouble(data.longitude));
                    }
                }

                mapFragment.getMapAsync(this);

                break;

            case Constants.LOCATION_SUCCESS:
                lat = String.valueOf(TEPreferences.readDouble(getActivity(), "latitude"));
                lng = String.valueOf(TEPreferences.readDouble(getActivity(), "longitude"));
                tvLocation.setText(TEPreferences.readString(getActivity(), "location"));
                break;

            case Constants.LABELS_SUCCESS:
                for (LabelsData.Data labelsData : LabelsManager.labelsList) {
                    labelsDataList.add(labelsData.name);
                }
                labelsAdapter.notifyDataSetChanged();
                break;

            case Constants.LISTING_EMPTY:
                Toast.makeText(getActivity(), R.string.no_listing, Toast.LENGTH_SHORT).show();
                break;

            case Constants.NO_RESPONSE:
                Toast.makeText(getActivity(), R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int counter = 0;
        for (int i=0; i<latList.size(); i++) {
            double latitude = latList.get(i);
            double longitude = lngList.get(i);
            LatLng latLng = new LatLng(latitude, longitude);
            builder.include(latLng) ;
            googleMap.addMarker(new MarkerOptions().position(latLng));
            counter++;
        }

        if (counter > 0) {
            LatLngBounds bounds = builder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.12);

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            googleMap.animateCamera(cu);
        }
    }

    @Override
    public void onTouch() {
        nestedScrollView.requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        for (CategoriesData.Data data : HomeManager.categoriesList) {
            data.setSelected(false);
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
                            tvLocation.setText(loc);
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
