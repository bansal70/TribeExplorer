package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/14/2017.
 */

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.beans.ListingData;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingManager {
    private static final String TAG = ListingManager.class.getSimpleName();
    public static List<ListingData.Data> dataList = new ArrayList<>();

    public void listingTask(String params) {
        APIService apiService = APIClient.getClient().create(APIService.class);
        Call<ListingData> call = apiService.listings(params);
        call.enqueue(new Callback<ListingData>() {
            @Override
            public void onResponse(Call<ListingData> call, Response<ListingData> response) {
                try {
                    ListingData listingData = response.body();
                    String status = listingData.status;
                    if (status.equalsIgnoreCase("success")) {
                        dataList = listingData.data;
                        EventBus.getDefault().postSticky(new Event(Constants.LISTING_SUCCESS, ""));
                    } else {
                        EventBus.getDefault().postSticky(new Event(Constants.LISTING_EMPTY, ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                }

            }

            @Override
            public void onFailure(Call<ListingData> call, Throwable t) {
                t.printStackTrace();
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }
}
