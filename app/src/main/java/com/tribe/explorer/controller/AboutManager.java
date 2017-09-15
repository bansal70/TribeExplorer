package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/15/2017.
 */

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.beans.AboutData;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutManager {
    public static List<AboutData.Data> dataList = new ArrayList<>();

    public void aboutTask(String params) {
        APIService apiService = APIClient.getClient().create(APIService.class);
        Call<AboutData> call = apiService.about(params);
        call.enqueue(new Callback<AboutData>() {
            @Override
            public void onResponse(Call<AboutData> call, Response<AboutData> response) {
                AboutData aboutData = response.body();
                String status = aboutData.status;
                if (status.equalsIgnoreCase("success")) {
                    dataList = aboutData.data;
                    EventBus.getDefault().postSticky(new Event(Constants.ABOUT_SUCCESS, ""));
                } else {
                    EventBus.getDefault().postSticky(new Event(Constants.ABOUT_EMPTY, ""));
                }

            }

            @Override
            public void onFailure(Call<AboutData> call, Throwable t) {
                t.printStackTrace();
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }
}
