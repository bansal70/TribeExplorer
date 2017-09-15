package com.tribe.explorer.controller;

/*
 * Created by win 10 on 9/14/2017.
 */

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.beans.LanguageData;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LanguageManager {
    public static List<LanguageData.Data> languagesList = new ArrayList<>();

    public void languageTask(String params) {
        APIService apiService = APIClient.getClient().create(APIService.class);
        Call<LanguageData> call = apiService.languages(params);
        call.enqueue(new Callback<LanguageData>() {
            @Override
            public void onResponse(Call<LanguageData> call, Response<LanguageData> response) {
                LanguageData languageData = response.body();
                String status = languageData.status;
                if (status.equalsIgnoreCase("success")) {
                    languagesList = languageData.data;
                    EventBus.getDefault().postSticky(new Event(Constants.LANGUAGE_SUCCESS, ""));
                } else {
                    EventBus.getDefault().postSticky(new Event(Constants.LANGUAGE_EMPTY, ""));
                }

            }

            @Override
            public void onFailure(Call<LanguageData> call, Throwable t) {
                t.printStackTrace();
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }
}
