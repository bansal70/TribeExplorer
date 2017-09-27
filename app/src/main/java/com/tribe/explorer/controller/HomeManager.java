package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/12/2017.
 */

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.beans.CategoriesData;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeManager {
    private static final String TAG = HomeManager.class.getSimpleName();
    public static List<CategoriesData.Data> categoriesList = new ArrayList<>();

    public void categoriesTask(String params) {
        APIService apiService = APIClient.getClient().create(APIService.class);
        Call<CategoriesData> call = apiService.categories(params);
        call.enqueue(new Callback<CategoriesData>() {
            @Override
            public void onResponse(Call<CategoriesData> call, Response<CategoriesData> response) {
                try {
                    CategoriesData categoriesData = response.body();
                    String status = categoriesData.status;
                    if (status.equalsIgnoreCase("success")) {
                        categoriesList = categoriesData.data;
                        EventBus.getDefault().postSticky(new Event(Constants.CATEGORIES_SUCCESS, ""));
                    } else {
                        EventBus.getDefault().postSticky(new Event(Constants.CATEGORIES_ERROR, ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                }
            }

            @Override
            public void onFailure(Call<CategoriesData> call, Throwable t) {
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }
}
