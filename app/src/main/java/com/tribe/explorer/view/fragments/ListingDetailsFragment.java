package com.tribe.explorer.view.fragments;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tribe.explorer.R;
import com.tribe.explorer.controller.ListingDetailsManager;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.ListingDetailsData;
import com.tribe.explorer.model.custom.MySupportMapFragment;
import com.tribe.explorer.view.adapters.LabelsAdapter;
import com.tribe.explorer.view.adapters.PhotoListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.defaultValue;

public class ListingDetailsFragment extends Fragment implements OnMapReadyCallback,
        SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, View.OnClickListener,
        MySupportMapFragment.OnTouchListener {

    private Dialog dialog;
    private List<ListingDetailsData.Data> listData;
    private ListingDetailsData.Data data;
    ImageView imgBack, imgFeatured;
    TextView tvTitle, tvLocation, tvCategory, tvTotalReviews, tvTotalFav, tvClaim,
            tvAddPhotos, tvDescription, tvContactAdmin, tvNoPhotos;
    RatingBar rbSpeed, rbQuality, rbPrice;
    Button btReview;
    SurfaceView videoView;
    MediaPlayer mediaPlayer = null;
    RecyclerView recyclerLabels, recyclerTimings, recyclerProfiles, recyclerGallery;
    PhotoListAdapter photoListAdapter;
    private List<String> galleryImages;
    private List<String> labelsList;
    LabelsAdapter labelsAdapter;
    TextView tvMonday, tvTuesday, tvWednesday, tvThursday, tvFriday, tvSaturday, tvSunday;
    double latitude =0.0, longitude = 0.0;
    MySupportMapFragment mapFragment;
    CardView imgDirections;
    GoogleMap googleMap;
    ScrollView scrollView;
    String user_id;
    int cat_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cat_id = bundle.getInt("cat_id", defaultValue);
            String lang = TEPreferences.readString(getActivity(), "lang");
            user_id = TEPreferences.readString(getActivity(), "user_id");
            listData = new ArrayList<>();
            dialog = Utils.showDialog(getActivity());
            dialog.show();
            ModelManager.getInstance().getListingDetailsManager()
                    .listingTask(Operations.getListingDetailsParams(cat_id, lang, user_id));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listing_details, container, false);
        initViews(view);

        return view;
    }

    public void initViews(View view) {
        tvTitle = view.findViewById(R.id.tvTitle);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvCategory = view.findViewById(R.id.tvCategory);
        tvTotalReviews = view.findViewById(R.id.tvTotalReviews);
        tvTotalFav = view.findViewById(R.id.tvTotalFav);
        tvClaim = view.findViewById(R.id.tvClaim);
        tvAddPhotos = view.findViewById(R.id.tvAddPhotos);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvContactAdmin = view.findViewById(R.id.tvContactAdmin);
        tvNoPhotos = view.findViewById(R.id.tvNoPhotos);

        tvMonday = view.findViewById(R.id.tvMonday);
        tvTuesday = view.findViewById(R.id.tvTuesday);
        tvWednesday = view.findViewById(R.id.tvWednesday);
        tvThursday = view.findViewById(R.id.tvThursday);
        tvFriday = view.findViewById(R.id.tvFriday);
        tvSaturday = view.findViewById(R.id.tvSaturday);
        tvSunday = view.findViewById(R.id.tvSunday);

        imgFeatured = view.findViewById(R.id.imgFeatured);
        imgBack = view.findViewById(R.id.imgBack);

        rbSpeed = view.findViewById(R.id.rbSpeed);
        rbQuality = view.findViewById(R.id.rbQuality);
        rbPrice = view.findViewById(R.id.rbPrice);

        btReview = view.findViewById(R.id.btReview);
        videoView = view.findViewById(R.id.videoView);
        imgDirections = view.findViewById(R.id.imgDirections);

        mapFragment = (MySupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.setListener(this);
        scrollView = view.findViewById(R.id.scrollView);

        recyclerLabels = view.findViewById(R.id.recyclerLabels);
        recyclerTimings = view.findViewById(R.id.recyclerTimings);
        recyclerProfiles = view.findViewById(R.id.recyclerProfiles);
        recyclerGallery = view.findViewById(R.id.recyclerGallery);

        galleryImages = new ArrayList<>();
        recyclerGallery.setHasFixedSize(true);
        recyclerGallery.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        DividerItemDecoration decor = new DividerItemDecoration(getActivity(),
                DividerItemDecoration.HORIZONTAL);
        decor.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerGallery.addItemDecoration(decor);
        photoListAdapter = new PhotoListAdapter(getActivity(), galleryImages);
        recyclerGallery.setAdapter(photoListAdapter);

        labelsList = new ArrayList<>();
        recyclerLabels.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        labelsAdapter = new LabelsAdapter(getActivity(), labelsList);
        recyclerLabels.setAdapter(labelsAdapter);

        imgBack.setOnClickListener(this);
        btReview.setOnClickListener(this);
        tvTotalReviews.setOnClickListener(this);

        if (!listData.isEmpty())
            setData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                backScreen();
                break;

            case R.id.btReview:
                if (!user_id.isEmpty())
                    selectFragment(new AddReviewFragment());
                else
                    Toast.makeText(getActivity(), R.string.login_to_rate, Toast.LENGTH_SHORT).show();
                break;

            case R.id.tvTotalReviews:
                selectFragment(new ReviewsFragment());
                break;
        }
    }

    public void setData() {
        data = listData.get(0);

        Utils.setImage(getActivity(), data.featured, imgFeatured);
        tvTitle.setText(data.title);
        tvLocation.setText(data.loc);
        tvCategory.setText(data.category.toString());
        tvDescription.setText(data.content);

        if (data.rating.size() > 0) {
            if (!data.rating.get(0).speed.isEmpty()) {
                float speed = Float.parseFloat(data.rating.get(0).speed);
                rbSpeed.setRating(speed);
            }
            if (!data.rating.get(0).quality.isEmpty()) {
                float quality = Float.parseFloat(data.rating.get(0).quality);
                rbQuality.setRating(quality);
            }
            if (!data.rating.get(0).price.isEmpty()) {
                float price = Float.parseFloat(data.rating.get(0).price);
                rbPrice.setRating(price);
            }
        }

        if (!data.latitude.isEmpty())
            latitude = Double.parseDouble(data.latitude);
        if (!data.longitude.isEmpty())
            longitude = Double.parseDouble(data.longitude);

        dialog.dismiss();
        galleryImages.addAll(data.galleryImages);
        if (galleryImages.size() == 0) {
            tvNoPhotos.setVisibility(View.VISIBLE);
        }
        photoListAdapter.notifyDataSetChanged();
        labelsList.addAll(data.label);
        labelsAdapter.notifyDataSetChanged();
        //playVideo();
        mapFragment.getMapAsync(this);
        setHours();
    }

    public void setHours() {
        if (data.jobHours == null) {
            return;
        }
        ListingDetailsData.JobHours jobHours = data.jobHours;
        
        List<ListingDetailsData.Mon> monday = jobHours.mon;
        StringBuilder str_mon = new StringBuilder();
        for (ListingDetailsData.Mon mon : monday) {
            str_mon.append(mon.open).append(" ").append(getString(R.string.to)).append(" ")
                    .append(mon.close).append(" ");
        }
        tvMonday.setText(str_mon.toString());

        List<ListingDetailsData.Tue> tuesday = jobHours.tue;
        StringBuilder str_tue = new StringBuilder();
        for (ListingDetailsData.Tue tue : tuesday) {
            str_tue.append(tue.open).append(" ").append(getString(R.string.to)).append(" ")
                    .append(tue.close).append(" ");
        }
        tvTuesday.setText(str_tue.toString());

        List<ListingDetailsData.Wed> wednesday = jobHours.wed;
        StringBuilder str_wed = new StringBuilder();
        for (ListingDetailsData.Wed wed : wednesday) {
            str_wed.append(wed.open).append(" ").append(getString(R.string.to)).append(" ")
                    .append(wed.close).append(" ");
        }
        tvWednesday.setText(str_wed.toString());

        List<ListingDetailsData.Thu> thursday = jobHours.thu;
        StringBuilder str_thu = new StringBuilder();
        for (ListingDetailsData.Thu thu : thursday) {
            str_thu.append(thu.open).append(" ").append(getString(R.string.to)).append(" ")
                    .append(thu.close).append(" ");
        }
        tvThursday.setText(str_thu.toString());

        List<ListingDetailsData.Fri> friday = jobHours.fri;
        StringBuilder str_fri = new StringBuilder();
        for (ListingDetailsData.Fri fri : friday) {
            str_fri.append(fri.open).append(" ").append(getString(R.string.to)).append(" ")
                    .append(fri.close).append(" ");
        }
        tvFriday.setText(str_fri.toString());

        List<ListingDetailsData.Sat> saturday = jobHours.sat;
        StringBuilder str_sat = new StringBuilder();
        for (ListingDetailsData.Sat sat : saturday) {
            str_sat.append(sat.open).append(" ").append(getString(R.string.to)).append(" ")
                    .append(sat.close).append(" ");
        }
        tvSaturday.setText(str_sat.toString());

        List<ListingDetailsData.Sun> sunday = jobHours.sun;
        StringBuilder str_sun = new StringBuilder();
        for (ListingDetailsData.Sun sun : sunday) {
            str_sun.append(sun.open).append(" ").append(getString(R.string.to)).append(" ")
                    .append(sun.close).append(" ");
        }
        tvSunday.setText(str_sun.toString());
    }

    public void playVideo() {
       // getActivity().startActivity(new Intent(getActivity(), VideoViewActivity.class));
        SurfaceHolder surfaceHolder = videoView.getHolder();
       // surfaceHolder.addCallback(getActivity());
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setDataSource("http://www.demonuts.com/Demonuts/smallvideo.mp4");
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        mediaPlayer.start();
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

        EventBus.getDefault().removeAllStickyEvents();
        switch (event.getKey()) {
            case Constants.LISTING_DETAILS_SUCCESS:
                listData.addAll(ListingDetailsManager.detailsList);
                setData();
                //  listingAdapter.notifyDataSetChanged();
                break;

            case Constants.LISTING_DETAILS_EMPTY:
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.no_listing, Toast.LENGTH_SHORT).show();
                break;

            case Constants.NO_RESPONSE:
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onTouch() {
        scrollView.requestDisallowInterceptTouchEvent(true);
    }

    public void selectFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("listing_id", cat_id+"");
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void backScreen() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
