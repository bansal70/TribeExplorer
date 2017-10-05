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
import com.tribe.explorer.view.adapters.FavoriteAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment{

    View view;
    RecyclerView recyclerListing;
    private FavoriteAdapter listingAdapter;
    private Dialog dialog;
    private List<ListingData.Data> listData;
    String user_id, lang;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listData = new ArrayList<>();
        user_id = TEPreferences.readString(getActivity(), "user_id");
        lang = TEPreferences.readString(getActivity(), "lang");
        if (user_id.isEmpty()) {
            Toast.makeText(getActivity(), R.string.login_to_check_fav, Toast.LENGTH_SHORT).show();
            return;
        }
        dialog = Utils.showDialog(getActivity());
        dialog.show();
        ModelManager.getInstance().getListingManager()
                .listingTask(Operations.getFavListParams(user_id, lang));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_favourites, container, false);
            initViews(view);
        }

        return view;
    }

    public void initViews(View view) {
        recyclerListing = view.findViewById(R.id.recyclerListings);
        recyclerListing.setLayoutManager(new LinearLayoutManager(getActivity()));

        listingAdapter = new FavoriteAdapter(getActivity(), listData);
        recyclerListing.setAdapter(listingAdapter);
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

}
