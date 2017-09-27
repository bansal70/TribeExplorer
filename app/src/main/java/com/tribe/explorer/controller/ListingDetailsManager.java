package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/19/2017.
 */

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.beans.ListingDetailsData;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingDetailsManager {
    private static final String TAG = ListingDetailsManager.class.getSimpleName();
    public static List<ListingDetailsData.Data> detailsList = new ArrayList<>();

    public void listingTask(String params) {
        APIService apiService = APIClient.getClient().create(APIService.class);
        Call<ListingDetailsData> call = apiService.listingsDetails(params);
        call.enqueue(new Callback<ListingDetailsData>() {
            @Override
            public void onResponse(Call<ListingDetailsData> call, Response<ListingDetailsData> response) {
                try {
                    ListingDetailsData listingDetailsData = response.body();
                    String status = listingDetailsData.status;
                    if (status.equalsIgnoreCase("success")) {
                        detailsList = listingDetailsData.data;
                        EventBus.getDefault().postSticky(new Event(Constants.LISTING_DETAILS_SUCCESS, ""));
                    } else {
                        EventBus.getDefault().postSticky(new Event(Constants.LISTING_DETAILS_EMPTY, ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                }

            }

            @Override
            public void onFailure(Call<ListingDetailsData> call, Throwable t) {
                t.printStackTrace();
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }
}
