package com.tribe.explorer.view.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tribe.explorer.R;
import com.tribe.explorer.controller.ListingManager;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.ListingData;
import com.tribe.explorer.view.adapters.ListingAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.defaultValue;

public class ListingFragment extends Fragment implements View.OnClickListener{

    RecyclerView recyclerListing;
    private ListingAdapter listingAdapter;
    private Dialog dialog;
    private List<ListingData.Data> listData;
    ImageView imgBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int cat_id = bundle.getInt("cat_id", defaultValue);
            String lang = TEPreferences.readString(getActivity(), "lang");
            String user_id = TEPreferences.readString(getActivity(), "user_id");
            listData = new ArrayList<>();
            dialog = Utils.showDialog(getActivity());
            dialog.show();
            ModelManager.getInstance().getListingManager()
                    .listingTask(Operations.getListingParams(cat_id, lang, user_id));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_listings, container, false);
        initViews(view);

        return view;
    }

    public void initViews(View view) {
        imgBack = view.findViewById(R.id.imgBack);
        recyclerListing = view.findViewById(R.id.recyclerListings);
        recyclerListing.setLayoutManager(new LinearLayoutManager(getActivity()));

        listingAdapter = new ListingAdapter(getActivity(), listData);
        recyclerListing.setAdapter(listingAdapter);

        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                backScreen();
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
                break;
            
            case Constants.LISTING_EMPTY:
                Toast.makeText(getActivity(), R.string.no_listing, Toast.LENGTH_SHORT).show();
                break;
            
            case Constants.NO_RESPONSE:
                Toast.makeText(getActivity(), R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void backScreen() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
