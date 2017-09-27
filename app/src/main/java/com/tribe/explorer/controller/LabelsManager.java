package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/26/2017.
 */

import android.content.Context;

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.beans.LabelsData;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LabelsManager {
    public static List<LabelsData.Data> labelsList = new ArrayList<>();

    public void labelsTask(final Context context, String params) {
        String lang = TEPreferences.readString(context, "lang");
        APIService apiService = APIClient.getClient().create(APIService.class);
        Call<LabelsData> call = apiService.getLabels(params);
        call.enqueue(new Callback<LabelsData>() {
            @Override
            public void onResponse(Call<LabelsData> call, Response<LabelsData> response) {
                LabelsData labelsData = response.body();
                String status = labelsData.status;
                try {
                    if (status.equalsIgnoreCase("success")) {
                        labelsList = labelsData.data;
                        EventBus.getDefault().postSticky(new Event(Constants.LABELS_SUCCESS, ""));
                    } else {
                        EventBus.getDefault().postSticky(new Event(Constants.LABELS_EMPTY, ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                }
            }

            @Override
            public void onFailure(Call<LabelsData> call, Throwable t) {
                t.printStackTrace();
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }
}